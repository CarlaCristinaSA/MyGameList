export interface Game {
  id: number;
  name: string;
  star_rating: number | null;
  developer: string;
  year: number;
  finished: boolean;
}

export type CreateGameInput = Omit<Game, 'id'>;
