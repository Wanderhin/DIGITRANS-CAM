import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommandesService } from '../../../core/services/commandes.service';
import { ClientsService } from '../../../core/services/clients.service';
import { RestaurantsService } from '../../../core/services/restaurants.service';
import { Client } from '../../../core/models/client.model';

@Component({
  selector: 'app-commande-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './commande-form.component.html',

})
export class CommandeFormComponent implements OnInit {
  commandeForm: FormGroup;
  clients: Client[] = [];
  restaurants: any[] = [];
  saving = false;
  errorMessage = '';
  totalMontant = 0;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private commandesService: CommandesService,
    private clientsService: ClientsService,
    private restaurantsService: RestaurantsService
  ) {
    this.commandeForm = this.fb.group({
      clientId: ['', Validators.required],
      restaurantId: ['', Validators.required],
      lignes: this.fb.array([])
    });
  }

  ngOnInit(): void {
    // Load clients for dropdown
    this.clientsService.search({ page: 0, size: 100 }).subscribe(res => {
      if (res.success) this.clients = res.data.content;
    });

    // Load restaurants for dropdown
    this.restaurantsService.list({ page: 0, size: 100 }).subscribe(res => {
      if (res.success) this.restaurants = res.data.content;
    });

    // Add first empty item line
    this.addLigne();
  }

  get lignes(): FormArray {
    return this.commandeForm.get('lignes') as FormArray;
  }

  addLigne(): void {
    const ligneGroup = this.fb.group({
      produitNom: ['', Validators.required],
      quantite: [1, [Validators.required, Validators.min(1)]],
      prixUnitaire: [0, [Validators.required, Validators.min(0)]]
    });
    this.lignes.push(ligneGroup);
    this.calculateTotal();
  }

  removeLigne(index: number): void {
    if (this.lignes.length > 1) {
      this.lignes.removeAt(index);
      this.calculateTotal();
    }
  }

  calculateTotal(): void {
    let total = 0;
    this.lignes.controls.forEach(control => {
      const q = control.get('quantite')?.value || 0;
      const p = control.get('prixUnitaire')?.value || 0;
      total += q * p;
    });
    this.totalMontant = total;
  }

  onSubmit(): void {
    if (this.commandeForm.invalid) return;
    this.saving = true;
    this.errorMessage = '';

    const req = this.commandeForm.value;
    req.clientId = +req.clientId;
    req.restaurantId = +req.restaurantId;

    this.commandesService.create(req).subscribe({
      next: (res) => {
        if (res.success) {
          this.router.navigate(['/commandes']);
        } else {
          this.errorMessage = res.message || "Une erreur est survenue lors de l'enregistrement de la commande.";
          this.saving = false;
        }
      },
      error: (err) => {
        this.errorMessage = err.error?.message || "Une erreur est survenue lors de l'enregistrement de la commande.";
        this.saving = false;
      }
    });
  }
}
