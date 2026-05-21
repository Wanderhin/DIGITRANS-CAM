output "ec2_public_ip" {
  description = "L'adresse IP publique de l'instance EC2"
  value       = aws_instance.backend_server.public_ip
}

output "frontend_bucket_url" {
  description = "L'URL d'hébergement statique du bucket S3 (Frontend)"
  value       = aws_s3_bucket_website_configuration.frontend_website.website_endpoint
}

output "logs_bucket_name" {
  description = "Le nom du bucket de logs"
  value       = aws_s3_bucket.logs_backup_bucket.bucket
}

output "private_key_pem" {
  description = "Clé privée générée pour l'accès SSH (à copier dans GitHub Secrets)"
  value       = tls_private_key.ssh_key.private_key_pem
  sensitive   = true
}
