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
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(12);

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

  // Resetar para página 1 quando filtros mudarem
  useMemo(() => {
    setCurrentPage(1);
  }, [searchTerm, filter, sortBy]);

  // Calcular paginação
  const totalPages = Math.ceil(filteredAndSortedGames.length / itemsPerPage);
  const paginatedGames = useMemo(() => {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    return filteredAndSortedGames.slice(startIndex, endIndex);
  }, [filteredAndSortedGames, currentPage, itemsPerPage]);

  const goToPage = (page: number) => {
    setCurrentPage(Math.max(1, Math.min(page, totalPages)));
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

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
            <span className="material-symbols-outlined stat-icon">sports_esports</span>
            <div className="stat-info">
              <span className="stat-value">{stats.total}</span>
              <span className="stat-label">Total de Jogos</span>
            </div>
          </div>
          <div className="stat-card">
            <span className="material-symbols-outlined stat-icon">check_circle</span>
            <div className="stat-info">
              <span className="stat-value">{stats.finished}</span>
              <span className="stat-label">Finalizados</span>
            </div>
          </div>
          <div className="stat-card">
            <span className="material-symbols-outlined stat-icon">pending</span>
            <div className="stat-info">
              <span className="stat-value">{stats.unfinished}</span>
              <span className="stat-label">Em Progresso</span>
            </div>
          </div>
          <div className="stat-card">
            <span className="material-symbols-outlined stat-icon">star</span>
            <div className="stat-info">
              <span className="stat-value">{stats.avgRating.toFixed(1)}</span>
              <span className="stat-label">Média de Avaliação</span>
            </div>
          </div>
        </div>

        <div className="search-container">
          <span className="material-symbols-outlined search-icon">search</span>
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
                <span className="material-symbols-outlined btn-icon-inline">check_circle</span>
                Finalizados
              </button>
              <button
                className={`filter-btn ${filter === 'unfinished' ? 'active' : ''}`}
                onClick={() => setFilter('unfinished')}
              >
                <span className="material-symbols-outlined btn-icon-inline">pending</span>
                Em Progresso
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
                <span className="material-symbols-outlined btn-icon-inline">sort_by_alpha</span>
                Nome
              </button>
              <button
                className={`sort-btn ${sortBy === 'rating' ? 'active' : ''}`}
                onClick={() => setSortBy('rating')}
              >
                <span className="material-symbols-outlined btn-icon-inline">star</span>
                Avaliação
              </button>
              <button
                className={`sort-btn ${sortBy === 'year' ? 'active' : ''}`}
                onClick={() => setSortBy('year')}
              >
                <span className="material-symbols-outlined btn-icon-inline">calendar_month</span>
                Ano
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
          <span className="material-symbols-outlined empty-icon">sports_esports</span>
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
            <span className="page-info">
              Página {currentPage} de {totalPages}
            </span>
          </div>
          <div className="games-grid">
            {paginatedGames.map((game) => (
              <GameCard
                key={game.id}
                game={game}
                onEdit={onEdit}
                onDelete={onDelete}
              />
            ))}
          </div>
          
          <div className="pagination">
              <button
                className="pagination-btn"
                onClick={() => goToPage(1)}
                disabled={currentPage === 1}
                title="Primeira página"
              >
                <span className="material-symbols-outlined">first_page</span>
              </button>
              <button
                className="pagination-btn"
                onClick={() => goToPage(currentPage - 1)}
                disabled={currentPage === 1}
                title="Página anterior"
              >
                <span className="material-symbols-outlined">chevron_left</span>
              </button>
              
              <div className="pagination-numbers">
                {Array.from({ length: totalPages }, (_, i) => i + 1)
                  .filter(page => {
                    // Mostrar primeira, última, atual e páginas próximas
                    if (page === 1 || page === totalPages) return true;
                    if (Math.abs(page - currentPage) <= 1) return true;
                    return false;
                  })
                  .map((page, idx, arr) => {
                    // Adicionar separador se houver salto
                    const prevPage = arr[idx - 1];
                    const showSeparator = prevPage && page - prevPage > 1;
                    
                    return (
                      <div key={page} style={{ display: 'flex', gap: '0.5rem' }}>
                        {showSeparator && <span className="pagination-separator">...</span>}
                        <button
                          className={`pagination-number ${page === currentPage ? 'active' : ''}`}
                          onClick={() => goToPage(page)}
                        >
                          {page}
                        </button>
                      </div>
                    );
                  })}
              </div>
              
              <button
                className="pagination-btn"
                onClick={() => goToPage(currentPage + 1)}
                disabled={currentPage === totalPages}
                title="Próxima página"
              >
                <span className="material-symbols-outlined">chevron_right</span>
              </button>
              <button
                className="pagination-btn"
                onClick={() => goToPage(totalPages)}
                disabled={currentPage === totalPages}
                title="Última página"
              >
                <span className="material-symbols-outlined">last_page</span>
              </button>
            </div>
        </>
      )}
    </div>
  );
}
