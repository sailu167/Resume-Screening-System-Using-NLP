import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.svm import LinearSVC
from sklearn.metrics import classification_report, accuracy_score
from sklearn.metrics.pairwise import cosine_similarity
import joblib
import os
import re

def clean_text(text):
    text = text.lower()
    text = re.sub(r'[^a-zA-Z\s]', '', text)
    # Custom domain stopwords
    stopwords = set(['and', 'the', 'is', 'in', 'at', 'of', 'for', 'with', 'a', 'an', 'to', 'experience', 'years', 'work', 'working', 'skills', 'knowledge', 'responsibilities', 'project', 'projects'])
    text = ' '.join([word for word in text.split() if word not in stopwords])
    return text

def train_model(csv_path):
    # Ensure the path is correct even if run from inside ml-service
    if not os.path.exists(csv_path):
        parent_csv_path = os.path.join('..', csv_path)
        if os.path.exists(parent_csv_path):
            csv_path = parent_csv_path
        else:
            print(f"Error: Could not find dataset at {csv_path} or {parent_csv_path}")
            return None

    print(f"Loading dataset from {csv_path}...")
    df = pd.read_csv(csv_path)
    
    print("Preprocessing text...")
    df['clean_resume'] = df['resume_text'].apply(clean_text)
    
    X = df['clean_resume']
    y = df['category']
    
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42, stratify=y)
    
    print("Vectorizing...")
    # N-gram (1,2) as requested
    vectorizer = TfidfVectorizer(ngram_range=(1, 2), max_features=5000)
    X_train_vec = vectorizer.fit_transform(X_train)
    X_test_vec = vectorizer.transform(X_test)
    
    print("Training LinearSVC...")
    # LinearSVC with class_weight='balanced' as requested
    clf = LinearSVC(class_weight='balanced', C=1.0, random_state=42)
    clf.fit(X_train_vec, y_train)
    
    y_pred = clf.predict(X_test_vec)
    acc = accuracy_score(y_test, y_pred)
    print(f"Accuracy: {acc:.4f}")
    print("\nClassification Report:\n", classification_report(y_test, y_pred))
    
    # Save model, vectorizer and metrics relative to this script
    base_dir = os.path.dirname(os.path.abspath(__file__))
    models_dir = os.path.join(base_dir, 'models')
    
    if not os.path.exists(models_dir):
        os.makedirs(models_dir)
        
    joblib.dump(clf, os.path.join(models_dir, 'svm_model.joblib'))
    joblib.dump(vectorizer, os.path.join(models_dir, 'tfidf_vectorizer.joblib'))
    
    metrics = {
        "accuracy": acc,
        "classification_report": classification_report(y_test, y_pred, output_dict=True)
    }
    joblib.dump(metrics, os.path.join(models_dir, 'metrics.joblib'))
    
    return acc

def predict_category(text):
    base_dir = os.path.dirname(os.path.abspath(__file__))
    models_dir = os.path.join(base_dir, 'models')
    
    clf = joblib.load(os.path.join(models_dir, 'svm_model.joblib'))
    vectorizer = joblib.load(os.path.join(models_dir, 'tfidf_vectorizer.joblib'))
    
    cleaned = clean_text(text)
    vec = vectorizer.transform([cleaned])
    prediction = clf.predict(vec)[0]
    return prediction

def calculate_similarity(resume_text, job_description):
    base_dir = os.path.dirname(os.path.abspath(__file__))
    models_dir = os.path.join(base_dir, 'models')
    
    vectorizer = joblib.load(os.path.join(models_dir, 'tfidf_vectorizer.joblib'))
    
    res_vec = vectorizer.transform([clean_text(resume_text)])
    jd_vec = vectorizer.transform([clean_text(job_description)])
    
    similarity = cosine_similarity(res_vec, jd_vec)[0][0]
    return float(similarity)

if __name__ == "__main__":
    train_model('dataset/resumes_augmented.csv')

