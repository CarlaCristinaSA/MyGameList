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
    const validRating = rating ?? 0;
    const filledStars = Math.round(validRating);
    return (
      <div className="stars">
        {Array.from({ length: maxStars }).map((_, i) => (
          <span key={i} className={i < filledStars ? 'star filled' : 'star'}>
            ★
          </span>
        ))}
      </div>
    );
  };

  const displayRating = game.star_rating ?? 0;
  
  const getRatingCategory = (rating: number) => {
    if (rating >= 4.5) return { label: 'Obra-Prima', className: 'masterpiece' };
    if (rating >= 4.0) return { label: 'Excelente', className: 'excellent' };
    if (rating >= 3.0) return { label: 'Bom', className: 'good' };
    if (rating >= 2.0) return { label: 'Regular', className: 'regular' };
    return { label: 'Fraco', className: 'weak' };
  };
  
  const ratingCategory = getRatingCategory(displayRating);

  return (
    <div className={`game-card ${ratingCategory.className}`}>
      <div className="card-badge-container">
        {game.finished === true && (
          <div className="finished-badge">
            <span className="badge-icon">✓</span>
            <span className="badge-text">Finalizado</span>
          </div>
        )}
        <div className={`rating-badge ${ratingCategory.className}`}>
          {ratingCategory.label}
        </div>
      </div>

      <div className="card-header">
        <div className="game-icon">🎮</div>
        <h3 className="game-title">{game.name}</h3>
      </div>

      <div className="card-content">
        <div className="info-row developer-row">
          <span className="info-icon">👨‍💻</span>
          <div className="info-text">
            <span className="info-label">Desenvolvedor</span>
            <span className="info-value">{game.developer}</span>
          </div>
        </div>

        <div className="info-row year-row">
          <span className="info-icon">📅</span>
          <div className="info-text">
            <span className="info-label">Ano de Lançamento</span>
            <span className="info-value">{game.year}</span>
          </div>
        </div>

        <div className="rating-container">
          <div className="rating-header">
            <span className="rating-icon">⭐</span>
            <span className="rating-label">Avaliação</span>
          </div>
          <div className="rating-display">
            {renderStars(game.star_rating)}
            <span className="rating-number">{displayRating.toFixed(1)}</span>
          </div>
        </div>
      </div>

      <div className="card-actions">
        <button className="btn-action btn-edit" onClick={() => onEdit(game)} title="Editar jogo">
          <span className="btn-icon">✎</span>
          <span className="btn-text">Editar</span>
        </button>
        <button className="btn-action btn-delete" onClick={() => onDelete(game.id)} title="Deletar jogo">
          <span className="btn-icon">🗑️</span>
          <span className="btn-text">Deletar</span>
        </button>
      </div>
    </div>
  );
}
