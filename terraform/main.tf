# 1. Bucket S3 pour le Frontend (Angular SPA)
resource "aws_s3_bucket" "frontend_bucket" {
  bucket        = "${var.project_name}-frontend-${random_string.suffix.result}"
  force_destroy = true
}

resource "random_string" "suffix" {
  length  = 6
  special = false
  upper   = false
}

resource "aws_s3_bucket_public_access_block" "frontend_public_access" {
  bucket = aws_s3_bucket.frontend_bucket.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_bucket_website_configuration" "frontend_website" {
  bucket = aws_s3_bucket.frontend_bucket.id

  index_document {
    suffix = "index.html"
  }

  error_document {
    key = "index.html"
  }
}

resource "aws_s3_bucket_policy" "frontend_policy" {
  bucket = aws_s3_bucket.frontend_bucket.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid       = "PublicReadGetObject"
        Effect    = "Allow"
        Principal = "*"
        Action    = "s3:GetObject"
        Resource  = "${aws_s3_bucket.frontend_bucket.arn}/*"
      },
    ]
  })
  
  depends_on = [aws_s3_bucket_public_access_block.frontend_public_access]
}

# 2. Bucket S3 Privé pour Logs & Backups
resource "aws_s3_bucket" "logs_backup_bucket" {
  bucket = "${var.project_name}-logs-backup-${random_string.suffix.result}"
}

# 3. Security Group pour EC2 (API Server)
resource "aws_security_group" "backend_sg" {
  name        = "${var.project_name}-backend-sg"
  description = "Allow SSH, HTTP API, and outbound VPN/internet traffic"

  # SSH Access
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # API Port (Spring Boot)
  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # All Outbound
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# 4. AMI Amazon Linux 2023 pour eu-north-1
data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-2023.*-x86_64"]
  }
}

# 5. Paire de clés pour l'accès SSH (Générée automatiquement)
resource "tls_private_key" "ssh_key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "aws_key_pair" "backend_key" {
  key_name   = "${var.project_name}-key"
  public_key = tls_private_key.ssh_key.public_key_openssh
}

# 6. EC2 Instance (API Server)
resource "aws_instance" "backend_server" {
  ami           = data.aws_ami.amazon_linux.id
  instance_type = "t3.micro"
  key_name      = aws_key_pair.backend_key.key_name

  vpc_security_group_ids = [aws_security_group.backend_sg.id]

  user_data = <<-EOF
              #!/bin/bash
              yum update -y
              # Installer Java 21 (Amazon Corretto)
              yum install -y java-21-amazon-corretto-headless
              
              mkdir -p /opt/app
              chown -R ec2-user:ec2-user /opt/app
              
              cat << 'SVC' > /etc/systemd/system/crm-backend.service
              [Unit]
              Description=CRM Backend API
              After=network.target

              [Service]
              User=ec2-user
              WorkingDirectory=/opt/app
              ExecStart=/usr/bin/java -jar /opt/app/crmBackend.jar
              SuccessExitStatus=143
              Restart=always
              RestartSec=10

              [Install]
              WantedBy=multi-user.target
              SVC
              
              systemctl daemon-reload
              EOF

  tags = {
    Name = "${var.project_name}-backend"
  }
}

# 7. CloudWatch Log Group
resource "aws_cloudwatch_log_group" "backend_logs" {
  name              = "/aws/ec2/${var.project_name}-backend"
  retention_in_days = 7
}
