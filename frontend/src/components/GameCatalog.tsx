import { useState, useMemo } from 'react';
import type { Game } from '../types/Game';
import { GameCard } from './GameCard';
import './GameCatalog.css';

interface GameCatalogProps {
  games: Game[];
  onEdit: (game: Game) => void;
  onDelete: (id: number) => void;
  isLoading?: boolean;
}

export function GameCatalog({
  games,
  onEdit,
  onDelete,
  isLoading = false,
}: GameCatalogProps) {
  const [searchTerm, setSearchTerm] = useState('');

  const filteredGames = useMemo(() => {
    return games.filter((game) =>
      game.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [games, searchTerm]);

  return (
    <div className="game-catalog">
      <div className="search-container">
        <input
          type="text"
          placeholder="Procurar por nome"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
      </div>

      {isLoading && <div className="loading">Carregando jogos...</div>}

      {!isLoading && filteredGames.length === 0 && (
        <div className="empty-state">
          {games.length === 0
            ? 'Nenhum jogo cadastrado. Comece adicionando um!'
            : 'Nenhum jogo encontrado com esse nome.'}
        </div>
      )}

      <div className="games-grid">
        {filteredGames.map((game) => (
          <GameCard
            key={game.id}
            game={game}
            onEdit={onEdit}
            onDelete={onDelete}
          />
        ))}
      </div>
    </div>
  );
}
