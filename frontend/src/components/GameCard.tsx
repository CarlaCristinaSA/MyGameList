import type { Game } from '../types/Game';
import './GameCard.css';

interface GameCardProps {
  game: Game;
  onEdit: (game: Game) => void;
  onDelete: (id: number) => void;
}

export function GameCard({ game, onEdit, onDelete }: GameCardProps) {
  const renderStars = (rating: number | null) => {
    const maxStars = 5;
    // Trata null ou undefined como 0
    const validRating = rating ?? 0;
    // Rating já está 0-5, não precisa dividir
    const filledStars = Math.round(validRating);
    return (
      <div className="stars">
        {Array.from({ length: maxStars }).map((_, i) => (
          <span key={i} className={i < filledStars ? 'star filled' : 'star'}>
            ⭐
          </span>
        ))}
      </div>
    );
  };

  // Rating já está em escala 0-5
  const displayRating = game.star_rating ?? 0;

  return (
    <div className="game-card">
      <div className="card-header">
        <h3>{game.name}</h3>
        {game.finished === true && <span className="finished-badge">✓ Finalizado</span>}
      </div>

      <div className="card-content">
        <p className="developer">
          <strong>Desenvolvedor:</strong> {game.developer}
        </p>
        <p className="year">
          <strong>Ano:</strong> {game.year}
        </p>

        <div className="rating">
          <strong>Rating:</strong> {renderStars(game.star_rating)}
          <span className="rating-number">{displayRating.toFixed(1)}/5</span>
        </div>
      </div>

      <div className="card-actions">
        <button className="btn-edit" onClick={() => onEdit(game)} title="Editar">
          ✎
        </button>
        <button className="btn-delete" onClick={() => onDelete(game.id)} title="Deletar">
          🗑️
        </button>
      </div>
    </div>
  );
}
