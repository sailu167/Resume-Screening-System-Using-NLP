# 📄 Resume Screening System using NLP

🚀 Resume Screening System using NLP | Data Science Domain  
👨‍💻 Project Lead: Bhatraju Sailu
## 📌Project Description
This project is developed to automate the resume screening process using Natural Language Processing (NLP). It helps recruiters efficiently filter and rank candidates based on job descriptions and skill matching.
## 🚀 Features
- 📂 Resume upload and parsing  
- 🔍 NLP-based skill extraction  
- 📊 Resume-job matching  
- 📈 Candidate ranking system  
- 👤 Role-based login (Candidate, Recruiter, Admin)  

## 🛠️ Tech Stack

- Frontend: React JS  
- Backend: Node JS  
- Database: PostgreSQL  
- Domain: Data Science
-  Language: Python
- Libraries:Pandas,NumPy
- Development Environment:Cursor
## 🧠 NLP Techniques Used

- Text Extraction  
- Tokenization  
- Stopword Removal  
- Lemmatization  
- TF-IDF / Similarity Matching

## 📊 Evaluation Metrics

The system performance was evaluated based on resume-job matching accuracy and NLP-based skill extraction.

- Accuracy of skill extraction
- Model Accuracy: 99.48%  
- Matching Efficiency: High relevance in candidate ranking  
- Improved shortlisting accuracy compared to manual screening  
- Resume-job matching score  
- Precision and recall for candidate selection  
- Ranking effectiveness
- Note: Accuracy may vary depending on dataset and job requirements.

## ⏱️ Performance

- Fast resume processing using NLP  
- Efficient candidate ranking system  
- Handles multiple resumes simultaneously

## 📁 Project Structure

- frontend/ → React JS code  
- backend/ → Node JS APIs  
- models/ → NLP models  
- dataset/ → Data used  
- screenshots/ → Project images
## ⚙️ Installation & Setup

### Step 1: Install Required Software
➡️ Make sure these are installed in your system:
- Node.js  
- Python  
- Java & Maven  
- Git  
- Antigravity
### Step 2: Clone the Repository
➡️ Open terminal and run:
git clone https://github.com/sailu167/resume-screening-system-nlp.git
cd resume-screening-system-nlp
### Step 3: Setup Backend
➡️ Go to backend folder:
cd backend
➡️ Install dependencies:
npm install
### Step 4: Setup Frontend
➡️ Open new terminal and run:
cd frontend
➡️ Install dependencies:
npm install
### Step 5: Setup ML Service
➡️ Open new terminal and run:
cd ml-service
➡️ Install Python packages:
pip install -r requirements.txt
### Step 6: Setup Antigravity Service
➡️ Open new terminal and run:
cd antigravity-service
➡️ Build project:
mvn clean install

# 🔷 ▶️ How to Run the Project
### Step 1: Start Backend
➡️ Open terminal:
cd backend
npm run dev
### Step 2: Start Frontend
➡️ Open new terminal:
cd frontend
npm start
### Step 3: Start ML Service
➡️ Open new terminal:
cd ml-service
python app.py
### Step 4: Open Application
➡️ Open browser and go to: 👉 http://localhost:3000⁠
### Step 5: Open frontend
➡️ Upload resume and view results

## 🔐 Security Features

- User authentication using JWT  
- Secure login for all roles  
- Data protection for user information

## 🧩 System Modules

The system is divided into three main modules to handle different functionalities in the recruitment process:

### 👤 Candidate Module
This module allows candidates to interact with the system by creating an account, uploading resumes, and applying for jobs. It also enables candidates to view job matches and track their application status.

### 🧑‍💼 Recruiter Module
The recruiter module helps recruiters manage the hiring process. Recruiters can post job requirements, view uploaded resumes, analyze candidate profiles, and access ranked results based on skill matching.

### 🛡️ Admin Module
The admin module provides full control over the system. Administrators can manage users, monitor system performance, and handle NLP models to ensure smooth functioning of the application.

## 👥 User Roles

The system supports multiple user roles, each with specific responsibilities:

### 👤 Candidate
- Register and login to the system  
- Upload resume  
- View job matches  
- Apply for jobs  
- Track application status  

### 🧑‍💼 Recruiter
- Login to the system  
- Post job requirements  
- View candidate resumes  
- Analyze ranked candidates  
- Shortlist suitable candidates  

### 🛡️ Admin
- Manage user accounts  
- Monitor system activities  
- Manage NLP models  
- Maintain system performance

## ⚙️ How It Works

The Resume Screening System follows a structured workflow to process resumes and match them with job requirements:

### 🔹 Step 1: Resume Upload
Candidates upload their resumes through the frontend interface.

### 🔹 Step 2: Data Extraction
The system uses NLP techniques to extract important information such as skills, education, and experience from the resumes.

### 🔹 Step 3: Job Posting
Recruiters create job descriptions by specifying required skills and qualifications.

### 🔹 Step 4: Matching Process
The system compares extracted resume data with job requirements using similarity algorithms (e.g., TF-IDF).

### 🔹 Step 5: Candidate Ranking
Candidates are ranked based on their relevance and matching score.

### 🔹 Step 6: Result Display
Recruiters can view ranked candidates, analyze profiles, and shortlist the best candidates for further selection.

🔰 ✅ Candidate Login Module

## 👤 Candidate Module

The Candidate module allows job seekers to interact with the system and apply for jobs efficiently.

### Features:
- User Registration and Login
- Resume Upload (PDF/DOC format)
- Automatic Resume Parsing using NLP
- Skill Extraction and Matching
- Job Search and Application
- View Applied Jobs and Status

### Functionality:
Candidates can upload their resumes, and the system analyzes the content using NLP techniques to extract skills, education, and experience. Based on this data, the system matches candidates with suitable job roles and generates an ATS score.

## 📸 Screenshots
### 👤 Candidate Login

The Candidate Login page allows users to securely access their account using credentials. It ensures authentication before accessing the system.

 ![Login](Candidate/login.png)

### 📊 Candidate Dashboard

The dashboard provides an overview of candidate activity, including job matches, application status, and profile details in a user-friendly interface.

![Dashboard](Candidate/dashboard.png)

### 💼 Job Listings

This page displays available job opportunities. Candidates can view job descriptions and find relevant matches based on their skills.

![Jobs](Candidate/jobs.png)

### 📄 Resume Upload

Candidates can upload their resumes in this section. The system uses NLP techniques to extract skills and relevant information from the resume.

![Resume](Candidate/resume.png)

### 📑 Applications

This page shows the list of jobs the candidate has applied for, along with application status and progress tracking.

![Applications](Candidate/applications.png)

🔰 ✅ Recruiter Login Module

## 🧑‍💼 Recruiter Module

The Recruiter module enables employers to manage job postings and screen candidates effectively.

### Features:
- Recruiter Login and Dashboard
- Job Posting and Management
- View Uploaded Resumes
- Candidate Ranking based on ATS Score
- Shortlisting Candidates
- Analytics Dashboard

### Functionality:
Recruiters can post job requirements and view candidate applications. The system uses Machine Learning and NLP to rank candidates based on their resumes. Recruiters can easily identify top candidates using ATS scores and filtering options.

## 🧑‍💼 Recruiter Login

The Recruiter Login allows recruiters to securely access the system and manage the hiring process efficiently.

### 📊 Recruiter Dashboard

The dashboard provides an overview of recruitment activities, including posted jobs, candidate matches, and overall system insights in a centralized view.

![Recruiter Dashboard](Recruiter/Recruiterdashboard.png)

### 💼 Job Posting

This page enables recruiters to create and publish job requirements. It allows specifying skills, experience, and job descriptions for better candidate matching.

![Job Posting](Recruiter/Jobposting.png)

### 📄 All Resumes

This section displays all uploaded resumes. Recruiters can review candidate profiles and filter resumes based on job requirements.

![All Resumes](Recruiter/AllResumes.png)

### 📈 Ranking

The system ranks candidates based on their resume relevance and skill matching using NLP techniques, helping recruiters identify the best candidates quickly.

![Ranking](Recruiter/Ranking.png)

### 📊 Analytics

This page provides insights and statistics related to recruitment, such as candidate performance, job trends, and matching accuracy.

![Analytics](Recruiter/Analytics.png)

🔰 ✅ Admin Login Module

## 🛠️ Admin Module

The Admin module provides full control over the system for managing users and system operations.

### Features:
- Admin Login and Dashboard
- User Management (Candidates & Recruiters)
- Model Management (ML Models)
- System Analytics and Reports
- Database Monitoring

### Functionality:
The admin oversees the entire system, manages users, monitors performance, and updates machine learning models. The admin ensures smooth functioning of the resume screening process and system security.

## 🛡️ Admin Login

The Admin Login provides secure access to system-level controls and management features. It allows administrators to monitor and maintain the overall application.

### 🔐 Admin Login Page

This page ensures secure authentication for administrators before accessing sensitive system functionalities.

![Admin Login](Admin/Adminloginpage.png)

### 📊 Admin Dashboard

The dashboard gives a complete overview of system performance, user activity, and key metrics in a centralized interface.

![Admin Dashboard](Admin/Admindashboard.png)

### 👥 User Management

This page enables the admin to manage users (candidates and recruiters), including adding, updating, and removing accounts.

![User Management](Admin/Usermanagement.png)

### 🤖 Model Management

This section allows the admin to manage and update NLP models used for resume parsing and candidate ranking, improving system accuracy.

![Model Management](Admin/Modelmanagement.png)

### 📈 System Analytics

Provides detailed insights into system usage, performance, and recruitment trends to support data-driven decisions.

![System Analytics](Admin/Systemanalytics.png)

## 📤 Output

The Resume Screening System successfully automates the process of analyzing and filtering resumes using NLP techniques.

### 🔹 Key Outputs
- Resumes are uploaded and processed automatically  
- Relevant information such as skills, education, and experience is extracted  
- Job descriptions are matched with candidate profiles  
- Candidates are ranked based on similarity and relevance

### 🔹 System Results
- Reduces manual effort in resume screening  
- Improves accuracy in candidate shortlisting  
- Speeds up the recruitment process  

### 🔹 Final Outcome
The system provides an efficient and intelligent solution for identifying the most suitable candidates, helping recruiters make faster and better hiring decisions.

## 🌟 Key Highlights

- Automated resume screening using NLP techniques  
- Intelligent candidate ranking system based on skill matching  
- Full Stack implementation (React, Node.js, PostgreSQL)  
- Integration of Machine Learning concepts for better accuracy  
- Role-based system (Candidate, Recruiter, Admin)  
- Reduces manual effort and improves efficiency in recruitment

## 🚀 Deployment

The system can be deployed using cloud platforms like AWS, Render, or Vercel for frontend and backend services.

## 🚀 Future Scope

- Enhance accuracy using advanced Machine Learning and Deep Learning models  
- Add AI-based job recommendation system  
- Integrate with real-time job portals (LinkedIn, Naukri, etc.)  
- Improve user interface for better user experience  
- Deploy the system on cloud platforms for scalability  
- Add multilingual resume support

## 📌 Conclusion

The Resume Screening System using NLP effectively automates the recruitment process by analyzing resumes and matching them with job requirements. By leveraging Natural Language Processing techniques, the system improves the efficiency and accuracy of candidate selection.

This project demonstrates a real-world application of Data Science and helps reduce manual effort while enabling faster and smarter hiring decisions.

## 🔍 Core Technology

The system uses Natural Language Processing (NLP) and Machine Learning algorithms such as TF-IDF and XGBoost to analyze resumes and generate ATS scores for candidate ranking.

## ❗ Challenges Faced

During the development of this project, several challenges were encountered:

- Handling resumes in different formats (PDF, DOCX) and extracting accurate text  
- Improving the accuracy of NLP-based skill extraction and matching  
- Integrating multiple technologies (React, Node.js, Python, Java) into a single system  
- Managing communication between backend and ML services  
- Ensuring proper data storage and retrieval using PostgreSQL  
- Handling large amounts of resume data efficiently

## 💡 Learnings

This project provided valuable hands-on experience and key learnings:

- Gained practical knowledge of Natural Language Processing (NLP) techniques  
- Improved understanding of full stack development (Frontend, Backend, Database)  
- Learned how to integrate multiple services (Node.js, Python, Java)  
- Developed problem-solving and debugging skills  
- Understood real-world challenges in recruitment automation  
- Enhanced teamwork and project management skills as a Project Lead

## 📚 References

- NLP documentation  
- React JS documentation  
- Node.js documentation  
- Research papers on resume screening
  
## 👨‍💻 Project Team

- Project Lead:Bhatraju Sailu
- Developed a Resume Screening System using NLP (Data Science Domain)
## Role:
- Project Lead handling Frontend (React JS), Backend (Node JS), Database (PostgreSQL), and NLP-based resume analysis
### 🔹 Responsibilities
- Lead the overall development of the Resume Screening System  
- Designed and developed frontend using React JS  
- Implemented backend APIs using Node JS  
- Integrated PostgreSQL database for data storage  
- Applied NLP techniques for resume parsing and skill extraction  
- Developed resume-job matching and ranking logic
- Managed project workflow and coordinated with team members
 ## 🏆 Achievements
- Successfully developed as final year project  
- Demonstrates real-world NLP application
  
## 📄 License
This project is licensed under the MIT License. 
## 📬 Contact

- Name :  BHATRAJU SAILU  
- Email: sailubhatraju3@gmail.com
- GitHub: https://github.com/sailu167⁠
