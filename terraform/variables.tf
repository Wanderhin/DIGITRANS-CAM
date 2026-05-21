variable "aws_region" {
  description = "La région AWS pour le déploiement"
  type        = string
  default     = "eu-north-1"
}

variable "project_name" {
  description = "Nom du projet utilisé comme préfixe"
  type        = string
  default     = "digitrans-crm"
}
