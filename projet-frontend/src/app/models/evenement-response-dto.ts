export interface EvenementResponseDTO {
    id: number;
    titre: string;
    description: string;
    date: string;
    lieu: string;
    capacite: number;
    actif: boolean;
    prix: number;
    categorieId: number;
    categorieNom: string;
    placesDisponibles: number;
  }