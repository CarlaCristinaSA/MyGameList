import { useState, useMemo, useEffect } from 'react';
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
  useEffect(() => {
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

  return (
    <div className="game-catalog">
      <div className="catalog-header">
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
                onClick={() => goToPage(currentPage - 1)}
                disabled={currentPage === 1}
                title="Anterior"
              >
                <span className="material-symbols-outlined">chevron_left</span>
              </button>
              
              <div className="pagination-numbers">
                {(() => {
                  const pages = [];
                  
                  // Sempre mostrar primeira página
                  pages.push(
                    <button
                      key={1}
                      className={`pagination-number ${currentPage === 1 ? 'active' : ''}`}
                      onClick={() => goToPage(1)}
                    >
                      1
                    </button>
                  );
                  
                  // Se a página atual está longe do início, adicionar separador
                  if (currentPage > 3) {
                    pages.push(<span key="sep1" className="pagination-separator">...</span>);
                  }
                  
                  // Mostrar páginas ao redor da atual
                  for (let i = Math.max(2, currentPage - 1); i <= Math.min(totalPages - 1, currentPage + 1); i++) {
                    pages.push(
                      <button
                        key={i}
                        className={`pagination-number ${currentPage === i ? 'active' : ''}`}
                        onClick={() => goToPage(i)}
                      >
                        {i}
                      </button>
                    );
                  }
                  
                  // Se a página atual está longe do fim, adicionar separador
                  if (currentPage < totalPages - 2) {
                    pages.push(<span key="sep2" className="pagination-separator">...</span>);
                  }
                  
                  // Sempre mostrar última página (se houver mais de uma)
                  if (totalPages > 1) {
                    pages.push(
                      <button
                        key={totalPages}
                        className={`pagination-number ${currentPage === totalPages ? 'active' : ''}`}
                        onClick={() => goToPage(totalPages)}
                      >
                        {totalPages}
                      </button>
                    );
                  }
                  
                  return pages;
                })()}
              </div>
              
              <button
                className="pagination-btn"
                onClick={() => goToPage(currentPage + 1)}
                disabled={currentPage === totalPages}
                title="Próxima"
              >
                <span className="material-symbols-outlined">chevron_right</span>
              </button>
            </div>
        </>
      )}
    </div>
  );
}
