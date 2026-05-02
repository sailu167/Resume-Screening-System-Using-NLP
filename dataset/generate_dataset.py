import pandas as pd
import random

# Categories as requested by the user
categories = [
    "Data Science", "Java Developer", "Frontend Developer", 
    "Full Stack Developer", "HR", "Tester", "DevOps"
]

skills_map = {
    "Data Science": ["Python", "Machine Learning", "Deep Learning", "Pandas", "NumPy", "Scikit-learn", "TensorFlow", "PyTorch", "SQL", "Data Visualization", "NLP"],
    "Java Developer": ["Java", "Spring Boot", "Hibernate", "Microservices", "REST API", "SQL", "Maven", "JUnit", "Multithreading", "JVM"],
    "Frontend Developer": ["React", "JavaScript", "HTML5", "CSS3", "TypeScript", "Redux", "Next.js", "Tailwind CSS", "Bootstrap", "Web Design"],
    "Full Stack Developer": ["Java", "Spring Boot", "React", "Node.js", "Express", "MongoDB", "SQL", "JavaScript", "REST API", "Docker"],
    "HR": ["Recruitment", "Talent Acquisition", "Employee Relations", "Payroll", "HR Policies", "Onboarding", "Training", "Strategic HR"],
    "Tester": ["Selenium", "Manual Testing", "Automation Testing", "Java", "Python", "JUnit", "TestNG", "Bug Tracking", "API Testing", "SDLC"],
    "DevOps": ["Docker", "Kubernetes", "Jenkins", "AWS", "Azure", "CI/CD", "Terraform", "Ansible", "Linux", "Scripting", "Monitoring"]
}

experience_templates = [
    "Experienced {category} with {years} years in the industry. Proficient in {skills}.",
    "Results-driven {category} professional with expertise in {skills}. Worked on several high-impact projects.",
    "Certified {category} expert with a strong background in {skills}. Passionate about building scalable solutions.",
    "{category} lead with {years} years of experience. Skilled at {skills} and team management.",
    "Junior {category} with a solid foundation in {skills}. Eager to contribute to innovative projects."
]

project_templates = [
    "Developed a {project_name} using {skills}.",
    "Implemented {project_name} which improved efficiency by {percent}%.",
    "Led the team in creating {project_name} with focus on {skills}.",
    "Architected a {project_name} using {skills} and deployed on {cloud}."
]

project_names = {
    "Data Science": ["Fraud Detection System", "Recommendation Engine", "Customer Churn Prediction", "Image Recognition App"],
    "Java Developer": ["Banking Microservices", "E-commerce Backend", "Inventory Management System", "Flight Booking API"],
    "Frontend Developer": ["Portfolio Website", "Dashboard UI", "E-learning Platform", "Social Media Feed"],
    "Full Stack Developer": ["SaaS Application", "Real-time Chat App", "Healthcare Portal", "Online Marketplace"],
    "HR": ["Employee Engagement Portal", "ATS Implementation", "Performance Review System", "Payroll Automation"],
    "Tester": ["E-commerce Test Suite", "Mobile App Automation", "Security Testing Framework", "Performance Benchmarking"],
    "DevOps": ["Kubernetes Deployment Pipeline", "Infrastructure as Code with Terraform", "Cloud Migration Project", "Monitoring Dashboard"]
}

def generate_resume(category):
    years = random.randint(1, 15)
    skills = ", ".join(random.sample(skills_map[category], k=min(6, len(skills_map[category]))))
    exp = random.choice(experience_templates).format(category=category, years=years, skills=skills)
    
    projects = []
    for _ in range(random.randint(1, 3)):
        p_name = random.choice(project_names[category])
        p_skills = ", ".join(random.sample(skills_map[category], k=3))
        p_template = random.choice(project_templates)
        
        # Fill template with available keys
        format_dict = {
            "project_name": p_name,
            "skills": p_skills,
            "percent": random.randint(10, 50),
            "cloud": random.choice(["AWS", "Azure", "GCP"])
        }
        projects.append(p_template.format(**format_dict))
    
    full_text = f"{exp} Projects: {' '.join(projects)}"
    return full_text

data = []
# Target 3000 resumes
resumes_per_category = 3000 // len(categories)

for category in categories:
    for _ in range(resumes_per_category):
        data.append({
            "resume_text": generate_resume(category),
            "category": category
        })

# Shuffle data
random.shuffle(data)

df = pd.DataFrame(data)
df.to_csv("dataset/resumes_augmented.csv", index=False)
print(f"Generated {len(df)} resumes across {len(categories)} categories.")

