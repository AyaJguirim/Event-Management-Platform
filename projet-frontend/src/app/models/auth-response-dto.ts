export interface AuthResponseDTO {
  accessToken: string;
  type: string;
  expiresIn: number;
  email: string;
  role: string;
  nom: string;
  prenom: string;
}