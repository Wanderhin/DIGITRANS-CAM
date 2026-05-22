import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ClientsService } from '../../../core/services/clients.service';
import { Client } from '../../../core/models/client.model';

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './client-form.component.html',

})
export class ClientFormComponent implements OnInit {
  clientForm: FormGroup;
  isEditMode = false;
  clientId?: number;
  saving = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private clientsService: ClientsService
  ) {
    this.clientForm = this.fb.group({
      prenom: ['', Validators.required],
      nom: ['', Validators.required],
      email: ['', [Validators.email]],
      telephone: ['', [Validators.required, Validators.pattern(/^\+237[0-9]{9}$/)]],
      ville: ['', Validators.required],
      adresse: [''],
      segment: ['REGULIER', Validators.required]
    });
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.clientId = +idParam;
      this.clientsService.getById(this.clientId).subscribe({
        next: (res) => {
          if (res.success) {
            this.clientForm.patchValue(res.data);
          }
        },
        error: (err) => {
          this.errorMessage = 'Impossible de charger les données du client.';
        }
      });
    }
  }

  onSubmit(): void {
    if (this.clientForm.invalid) return;
    this.saving = true;
    this.errorMessage = '';

    const clientData: Client = this.clientForm.value;

    const request$ = this.isEditMode && this.clientId
      ? this.clientsService.update(this.clientId, clientData)
      : this.clientsService.create(clientData);

    request$.subscribe({
      next: (res) => {
        if (res.success) {
          this.router.navigate(['/clients']);
        } else {
          this.errorMessage = res.message || "Une erreur est survenue lors de l'enregistrement.";
          this.saving = false;
        }
      },
      error: (err) => {
        this.errorMessage = err.error?.message || "Une erreur est survenue lors de l'enregistrement.";
        this.saving = false;
      }
    });
  }
}
