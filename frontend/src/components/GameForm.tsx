import { useState, useEffect } from 'react';
import type { CreateGameInput, Game } from '../types/Game';
import './GameForm.css';

interface GameFormProps {
  onSubmit: (game: CreateGameInput) => void;
  isLoading?: boolean;
  initialData?: Game | null;
  onCancel?: () => void;
}

export function GameForm({ onSubmit, isLoading = false, initialData, onCancel }: GameFormProps) {
  const [formData, setFormData] = useState<CreateGameInput>({
    name: '',
    developer: '',
    year: new Date().getFullYear(),
    star_rating: 5,
    finished: false,
  });
  const [hoveredStar, setHoveredStar] = useState<number | null>(null);

  useEffect(() => {
    if (initialData) {
      setFormData({
        name: initialData.name,
        developer: initialData.developer,
        year: initialData.year || new Date().getFullYear(),
        star_rating: initialData.star_rating ?? 5,
        finished: initialData.finished ?? false,
      });
    } else {
      setFormData({
        name: '',
        developer: '',
        year: new Date().getFullYear(),
        star_rating: 5,
        finished: false,
      });
    }
  }, [initialData]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value, type } = e.target as HTMLInputElement;
    setFormData((prev) => ({
      ...prev,
      [name]:
        type === 'checkbox'
          ? (e.target as HTMLInputElement).checked
          : type === 'number'
            ? value === '' ? 0 : parseFloat(value)
            : value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (formData.name.trim() && formData.developer.trim()) {
      onSubmit(formData);
      // Limpa o formulário após envio
      setFormData({
        name: '',
        developer: '',
        year: new Date().getFullYear(),
        star_rating: 5,
        finished: false,
      });
    }
  };

  const isEditing = initialData !== null && initialData !== undefined;

  return (
    <form className="game-form" onSubmit={handleSubmit}>
      <div className="form-group">
        <label htmlFor="name">Nome</label>
        <input
          type="text"
          id="name"
          name="name"
          value={formData.name}
          onChange={handleChange}
          placeholder="Nome do jogo"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="developer">Desenvolvedor</label>
        <input
          type="text"
          id="developer"
          name="developer"
          value={formData.developer}
          onChange={handleChange}
          placeholder="Desenvolvedor"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="year">Ano</label>
        <input
          type="number"
          id="year"
          name="year"
          value={formData.year}
          onChange={handleChange}
          placeholder="Ano"
        />
      </div>

      <div className="form-group star-rating-group">
        <label htmlFor="star_rating">Avaliação</label>
        <div className="star-rating-input">
          {[1, 2, 3, 4, 5].map((star) => (
            <button
              key={star}
              type="button"
              className={`star-button ${
                star <= (hoveredStar ?? formData.star_rating ?? 0) ? 'filled' : ''
              }`}
              onMouseEnter={() => setHoveredStar(star)}
              onMouseLeave={() => setHoveredStar(null)}
              onClick={() => setFormData((prev) => ({ ...prev, star_rating: star }))}
            >
              ⭐
            </button>
          ))}
          <span className="rating-display">{formData.star_rating}/5</span>
        </div>
      </div>

      <div className="form-group checkbox">
        <input
          type="checkbox"
          id="finished"
          name="finished"
          checked={formData.finished}
          onChange={handleChange}
        />
        <label htmlFor="finished">Finalizado</label>
      </div>

      <div className="form-actions">
        {onCancel && (
          <button type="button" className="cancel-button" onClick={onCancel}>
            <span className="material-symbols-outlined">close</span>
            Cancelar
          </button>
        )}
        <button type="submit" className="submit-button" disabled={isLoading}>
          <span className="material-symbols-outlined">
            {isLoading ? 'hourglass_empty' : isEditing ? 'edit' : 'add_circle'}
          </span>
          {isLoading 
            ? 'Salvando...' 
            : isEditing 
              ? 'Atualizar Jogo' 
              : 'Cadastrar Jogo'
          }
        </button>
      </div>
    </form>
  );
}
