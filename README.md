# AWS Dynamic Web Application – Step by Step Deployment

This project demonstrates how to design and deploy a simple dynamic web application on AWS using core cloud services.

## Project Goal

Deploy a web application with:

- Backend (Java Application on EC2)
- Database (MySQL on RDS)

Using secure and scalable cloud architecture.

---

# Architecture Overview

User  
↓  
Application Load Balancer  
↓  
EC2 (Java Backend – Public Subnet)  
↓  
RDS MySQL (Private Subnet)  

Optional:
S3 → Static Files  
CloudWatch → Monitoring  

---

# AWS Services Used

- Amazon VPC  
- Amazon EC2  
- Amazon RDS  
- Internet Gateway  
- Route Tables  
- Security Groups  
- Application Load Balancer  
- Amazon S3 (Optional)  
- Amazon CloudWatch  

---

# Step-by-Step Deployment

---

## STEP 1: Create VPC

1. Go to AWS Console → VPC  
2. Click Create VPC  
3. Configure:
   - Name: My-WebApp-VPC  
   - CIDR Block: 10.0.0.0/16  
   - Tenancy: Default  
4. Click Create  

✔ Creates private network for your application.

---

## STEP 2: Create Subnets

Inside the VPC create:

### 🔹 Public Subnet
- Name: Public-Subnet  
- CIDR: 10.0.1.0/24  

### 🔹 Private Subnet
- Name: Private-Subnet  
- CIDR: 10.0.2.0/24  

✔ Public subnet → Web server  
✔ Private subnet → Database  

---

## STEP 3: Create Internet Gateway

1. Go to Internet Gateways  
2. Create Internet Gateway  
3. Attach to your VPC  

✔ Allows public subnet to access internet.

---

## STEP 4: Configure Route Table

1. Create Route Table  
2. Add Route:
   - Destination: 0.0.0.0/0  
   - Target: Internet Gateway  
3. Associate with Public Subnet  

✔ Public subnet now has internet access.

---

## STEP 5: Launch EC2 (Backend Server)

1. Go to EC2 → Launch Instance  
2. Choose:
   - AMI: Amazon Linux  
   - Instance Type: t2.micro  
3. Select:
   - VPC: My-WebApp-VPC  
   - Subnet: Public-Subnet  
   - Auto Assign Public IP: Enable  

### Security Group Rules

| Type | Port | Source |
|------|------|--------|
| SSH  | 22   | My IP |
| HTTP | 80   | Anywhere |
| HTTPS| 443  | Anywhere |

Launch instance.

---

## STEP 6: Install Java on EC2

Connect via SSH:

```
ssh -i my-key.pem ec2-user@<Public-IP>
```

Install Java:
```
sudo dnf install java-17-amazon-corretto -y
```
Check:
```
java -version
```

---

## STEP 7: Create RDS Database

1. Go to RDS → Create Database  
2. Choose:
   - Engine: MySQL  
   - Public Access: No  
   - VPC: My-WebApp-VPC  
   - Subnet: Private-Subnet  

### RDS Security Group

Allow:
- Port 3306  
- Source → EC2 Security Group ONLY  

Create database.

Copy RDS endpoint.

---

## STEP 8: Connect EC2 to RDS

Install MySQL client:
```
sudo dnf install mysql -y
```
Connect:
```
mysql -h <RDS-ENDPOINT> -u admin -p
```
Create database:
```
CREATE DATABASE myappdb;
USE myappdb;

CREATE TABLE users (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100),
email VARCHAR(100),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## STEP 9: Deploy Java Application

Compile:
```
javac -cp .:mysql-connector-j-8.0.33.jar DBTest.java
```
Run:
```
java -cp .:mysql-connector-j-8.0.33.jar DBTest
```

If successful, backend is connected to RDS.

---

# Security Best Practices

- RDS in Private Subnet  
- No public DB access  
- Security Groups restrict traffic  
- SSH allowed only from specific IP  
- Use IAM roles instead of hardcoded credentials  

---

# Final Network Structure

VPC (10.0.0.0/16)
│
├── Public Subnet (10.0.1.0/24)
│     ├── EC2
│     └── Load Balancer
│
└── Private Subnet (10.0.2.0/24)
      └── RDS

---

