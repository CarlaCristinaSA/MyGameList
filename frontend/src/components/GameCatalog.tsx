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

type FilterType = 'all' | 'finished' | 'unfinished';
type SortType = 'name' | 'rating' | 'year';

export function GameCatalog({
  games,
  onEdit,
  onDelete,
  isLoading = false,
}: GameCatalogProps) {
  const [searchTerm, setSearchTerm] = useState('');
  const [filter, setFilter] = useState<FilterType>('all');
  const [sortBy, setSortBy] = useState<SortType>('name');

  const filteredAndSortedGames = useMemo(() => {
    let result = games.filter((game) =>
      game.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // Aplicar filtro
    if (filter === 'finished') {
      result = result.filter((game) => game.finished === true);
    } else if (filter === 'unfinished') {
      result = result.filter((game) => game.finished !== true);
    }

    // Aplicar ordenação
    result = [...result].sort((a, b) => {
      switch (sortBy) {
        case 'rating':
          return (b.star_rating ?? 0) - (a.star_rating ?? 0);
        case 'year':
          return b.year - a.year;
        case 'name':
        default:
          return a.name.localeCompare(b.name);
      }
    });

    return result;
  }, [games, searchTerm, filter, sortBy]);

  const stats = useMemo(() => {
    const total = games.length;
    const finished = games.filter(g => g.finished === true).length;
    const avgRating = games.length > 0 
      ? games.reduce((acc, g) => acc + (g.star_rating ?? 0), 0) / games.length 
      : 0;
    return { total, finished, unfinished: total - finished, avgRating };
  }, [games]);

  return (
    <div className="game-catalog">
      <div className="catalog-header">
        <div className="stats-container">
          <div className="stat-card">
            <span className="stat-icon">🎮</span>
            <div className="stat-info">
              <span className="stat-value">{stats.total}</span>
              <span className="stat-label">Total de Jogos</span>
            </div>
          </div>
          <div className="stat-card">
            <span className="stat-icon">✓</span>
            <div className="stat-info">
              <span className="stat-value">{stats.finished}</span>
              <span className="stat-label">Finalizados</span>
            </div>
          </div>
          <div className="stat-card">
            <span className="stat-icon">⏳</span>
            <div className="stat-info">
              <span className="stat-value">{stats.unfinished}</span>
              <span className="stat-label">Em Progresso</span>
            </div>
          </div>
          <div className="stat-card">
            <span className="stat-icon">⭐</span>
            <div className="stat-info">
              <span className="stat-value">{stats.avgRating.toFixed(1)}</span>
              <span className="stat-label">Média de Avaliação</span>
            </div>
          </div>
        </div>

        <div className="search-container">
          <span className="search-icon">🔍</span>
          <input
            type="text"
            placeholder="Procurar por nome do jogo..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>

        <div className="filters-container">
          <div className="filter-group">
            <label className="filter-label">Filtrar por:</label>
            <div className="filter-buttons">
              <button
                className={`filter-btn ${filter === 'all' ? 'active' : ''}`}
                onClick={() => setFilter('all')}
              >
                Todos
              </button>
              <button
                className={`filter-btn ${filter === 'finished' ? 'active' : ''}`}
                onClick={() => setFilter('finished')}
              >
                ✓ Finalizados
              </button>
              <button
                className={`filter-btn ${filter === 'unfinished' ? 'active' : ''}`}
                onClick={() => setFilter('unfinished')}
              >
                ⏳ Em Progresso
              </button>
            </div>
          </div>

          <div className="filter-group">
            <label className="filter-label">Ordenar por:</label>
            <div className="sort-buttons">
              <button
                className={`sort-btn ${sortBy === 'name' ? 'active' : ''}`}
                onClick={() => setSortBy('name')}
              >
                📝 Nome
              </button>
              <button
                className={`sort-btn ${sortBy === 'rating' ? 'active' : ''}`}
                onClick={() => setSortBy('rating')}
              >
                ⭐ Avaliação
              </button>
              <button
                className={`sort-btn ${sortBy === 'year' ? 'active' : ''}`}
                onClick={() => setSortBy('year')}
              >
                📅 Ano
              </button>
            </div>
          </div>
        </div>
      </div>

      {isLoading && (
        <div className="loading">
          <div className="loading-spinner"></div>
          <p>Carregando jogos...</p>
        </div>
      )}

      {!isLoading && filteredAndSortedGames.length === 0 && (
        <div className="empty-state">
          <span className="empty-icon">🎮</span>
          <h3 className="empty-title">
            {games.length === 0
              ? 'Nenhum jogo cadastrado'
              : 'Nenhum jogo encontrado'}
          </h3>
          <p className="empty-description">
            {games.length === 0
              ? 'Comece adicionando seus jogos favoritos!'
              : 'Tente ajustar os filtros ou buscar por outro termo.'}
          </p>
        </div>
      )}

      {!isLoading && filteredAndSortedGames.length > 0 && (
        <>
          <div className="results-info">
            <span className="results-count">
              {filteredAndSortedGames.length} {filteredAndSortedGames.length === 1 ? 'jogo encontrado' : 'jogos encontrados'}
            </span>
          </div>
          <div className="games-grid">
            {filteredAndSortedGames.map((game) => (
              <GameCard
                key={game.id}
                game={game}
                onEdit={onEdit}
                onDelete={onDelete}
              />
            ))}
          </div>
        </>
      )}
    </div>
  );
}
