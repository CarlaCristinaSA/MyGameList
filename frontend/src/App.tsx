import { useState, useEffect } from 'react';
import { Navbar, Home, GameForm, GameCatalog } from './components';
import type { Game, CreateGameInput } from './types/Game';
import { useGamesAPI } from './services';
import './App.css';

function App() {
  const [activeTab, setActiveTab] = useState<'home' | 'cadastro' | 'catalogo'>('home');
  const [games, setGames] = useState<Game[]>([]);
  const [editingGame, setEditingGame] = useState<Game | null>(null);
  const [apiError, setApiError] = useState<string | null>(null);
  
  const { fetchGames, createGame, updateGame, deleteGame, isLoading, error } = useGamesAPI();

  // Carrega jogos ao montar o componente
  useEffect(() => {
    loadGames();
  }, []);

  // Monitora erros da API
  useEffect(() => {
    if (error) {
      setApiError(error);
      console.error('Erro na API:', error);
    }
  }, [error]);

  const loadGames = async () => {
    const fetchedGames = await fetchGames();
    setGames(fetchedGames);
  };

  // Função para adicionar um novo jogo
  const handleAddGame = async (newGame: CreateGameInput) => {
    const result = await createGame(newGame);
    if (result) {
      setGames((prev) => [result, ...prev]);
      alert('Jogo adicionado com sucesso!');
      setEditingGame(null);
      setActiveTab('catalogo'); // Vai direto para o catálogo após cadastrar
    } else {
      alert('Erro ao adicionar jogo!');
    }
  };

  // Função para editar um jogo
  const handleEditGame = (game: Game) => {
    setEditingGame(game);
    setActiveTab('cadastro');
  };

  // Função para atualizar um jogo
  const handleUpdateGame = async (updatedGame: Game) => {
    const result = await updateGame(updatedGame);
    if (result) {
      setGames((prev) =>
        prev.map((game) => (game.id === updatedGame.id ? result : game))
      );
      alert('Jogo atualizado com sucesso!');
      setEditingGame(null);
      setActiveTab('catalogo'); // Vai direto para o catálogo após atualizar
    } else {
      alert('Erro ao atualizar jogo!');
    }
  };

  // Função para deletar um jogo
  const handleDeleteGame = async (id: number) => {
    if (window.confirm('Deseja deletar este jogo?')) {
      const success = await deleteGame(id);
      if (success) {
        setGames((prev) => prev.filter((game) => game.id !== id));
        alert('Jogo deletado com sucesso!');
      } else {
        alert('Erro ao deletar jogo!');
      }
    }
  };

  const handleSubmitForm = (formData: CreateGameInput) => {
    if (editingGame) {
      handleUpdateGame({ ...editingGame, ...formData });
    } else {
      handleAddGame(formData);
    }
  };

  const stats = {
    total: games.length,
    finished: games.filter(g => g.finished).length,
    avgRating: games.length > 0 
      ? (games.reduce((acc, g) => acc + (g.star_rating || 0), 0) / games.length).toFixed(1)
      : '0.0'
  };

  return (
    <div className="app">
      <Navbar activeTab={activeTab} onTabChange={setActiveTab} />

      {apiError && (
        <div className="api-error">
          <strong>⚠️ Erro na API:</strong> {apiError}
          <br />
          <small>Verifique se a API está rodando em http://localhost:8000</small>
          <button onClick={() => setApiError(null)}>✕</button>
        </div>
      )}

      <main className="app-main">
        {activeTab === 'home' && (
          <Home 
            stats={stats} 
            onNavigate={(tab) => setActiveTab(tab)} 
          />
        )}

        {activeTab === 'cadastro' && (
          <div className="tab-content">
            <GameForm 
              onSubmit={handleSubmitForm} 
              isLoading={isLoading}
              initialData={editingGame}
            />
          </div>
        )}

        {activeTab === 'catalogo' && (
          <div className="tab-content">
            <GameCatalog
              games={games}
              onEdit={handleEditGame}
              onDelete={handleDeleteGame}
              isLoading={isLoading}
            />
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
