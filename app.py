from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import os
from model import clean_text, calculate_similarity
import PyPDF2
import docx

app = Flask(__name__)
CORS(app)

# Load model and vectorizer
base_dir = os.path.dirname(os.path.abspath(__file__))
models_dir = os.path.join(base_dir, 'models')

clf = joblib.load(os.path.join(models_dir, 'svm_model.joblib'))
vectorizer = joblib.load(os.path.join(models_dir, 'tfidf_vectorizer.joblib'))
metrics = joblib.load(os.path.join(models_dir, 'metrics.joblib'))

def extract_text_from_pdf(file):
    reader = PyPDF2.PdfReader(file)
    text = ""
    for page in reader.pages:
        text += page.extract_text()
    return text

def extract_text_from_docx(file):
    doc = docx.Document(file)
    text = ""
    for para in doc.paragraphs:
        text += para.text + "\n"
    return text

@app.route('/predict', methods=['POST'])
def predict():
    if 'file' in request.files:
        file = request.files['file']
        if file.filename.endswith('.pdf'):
            text = extract_text_from_pdf(file)
        elif file.filename.endswith('.docx'):
            text = extract_text_from_docx(file)
        else:
            return jsonify({"error": "Unsupported file format"}), 400
    else:
        text = request.json.get('text', '')

    if not text:
        return jsonify({"error": "No text provided"}), 400

    cleaned = clean_text(text)
    vec = vectorizer.transform([cleaned])
    prediction = clf.predict(vec)[0]
    
    return jsonify({
        "category": prediction,
        "extracted_text": text,
        "raw_text_preview": text[:500] + "..."
    })

@app.route('/rank', methods=['POST'])
def rank():
    data = request.json
    resume_text = data.get('resume_text')
    job_description = data.get('job_description')
    
    if not resume_text or not job_description:
        return jsonify({"error": "Missing resume_text or job_description"}), 400
    
    score = calculate_similarity(resume_text, job_description)
    
    # Simple skill extraction (mock for now based on keywords)
    skills = ["Python", "Java", "React", "Docker", "AWS", "SQL", "Spring Boot"]
    found_skills = [s for s in skills if s.lower() in resume_text.lower()]
    
    return jsonify({
        "score": round(score * 100, 2),
        "matched_skills": found_skills
    })

@app.route('/metrics', methods=['GET'])
def get_metrics():
    return jsonify(metrics)

if __name__ == '__main__':
    app.run(port=5000, debug=True)

