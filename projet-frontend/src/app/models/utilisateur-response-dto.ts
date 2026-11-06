export interface UtilisateurResponseDTO {
    id: number;
    nom: string;
    prenom: string;
    email: string;
    role: string;
    actif: boolean;
    dateCreation: string;
    emailVerified: boolean;
  }