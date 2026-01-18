/**
 * API Configuration
 * 
 * Endpoints disponíveis:
 * GET    /api/game/v2                    - Get All Games V2
 * GET    /api/game/v2/{id}               - Get Game by ID V2
 * GET    /api/game/v2/findGameByName/{name} - Get Games by Name V2
 * POST   /api/game/v2                    - Create a game V2 (envia objeto completo com id)
 * PUT    /api/game/v2                    - Update a game (envia objeto completo com id no body)
 */

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

console.log('API Base URL:', API_BASE_URL);

import type { Game } from '../types/Game';

export const api = {
  games: {
    // GET /api/game/v2 - Retorna todos os jogos
    getAll: async (): Promise<Game[]> => {
      try {
        console.log('Buscando jogos em:', `${API_BASE_URL}/game/v2`);
        const response = await fetch(`${API_BASE_URL}/game/v2`);
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log('Resposta da API:', data);
        
        // A API retorna um objeto com estrutura HAL (_embedded, _links, page)
        // Extrair os jogos de _embedded.gameDTOV2List
        let games: Game[] = [];
        if (data._embedded && data._embedded.gameDTOV2List) {
          games = data._embedded.gameDTOV2List;
        } else if (data._embedded && data._embedded.games) {
          games = data._embedded.games;
        } else if (Array.isArray(data)) {
          games = data;
        } else if (data.games && Array.isArray(data.games)) {
          games = data.games;
        }
        
        console.log('Jogos processados:', games);
        return games;
      } catch (error) {
        const errorMsg = error instanceof Error 
          ? error.message 
          : 'Erro desconhecido ao buscar jogos';
        console.error('Erro ao buscar jogos:', errorMsg);
        throw new Error(`Falha ao conectar à API: ${errorMsg}. Verifique se a API está rodando em ${API_BASE_URL}`);
      }
    },

    // GET /api/game/v2/{id} - Retorna um jogo específico
    getById: async (id: number): Promise<Game> => {
      try {
        const response = await fetch(`${API_BASE_URL}/game/v2/${id}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return await response.json();
      } catch (error) {
        console.error(`Erro ao buscar jogo ${id}:`, error);
        throw error;
      }
    },

    // GET /api/game/v2/findGameByName/{name} - Busca por nome
    findByName: async (name: string): Promise<Game[]> => {
      try {
        const response = await fetch(`${API_BASE_URL}/game/v2/findGameByName/${encodeURIComponent(name)}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const data = await response.json();
        
        // Mesmo padrão HAL - pode ser gameDTOV2List ou games
        let games: Game[] = [];
        if (data._embedded && data._embedded.gameDTOV2List) {
          games = data._embedded.gameDTOV2List;
        } else if (data._embedded && data._embedded.games) {
          games = data._embedded.games;
        } else if (Array.isArray(data)) {
          games = data;
        } else if (data.games && Array.isArray(data.games)) {
          games = data.games;
        }
        
        return games;
      } catch (error) {
        console.error(`Erro ao buscar jogo por nome "${name}":`, error);
        throw error;
      }
    },

    // POST /api/game/v2 - Cria um jogo (não envia id, será auto-gerado)
    create: async (game: Omit<Game, 'id'>): Promise<Game> => {
      try {
        console.log('Enviando jogo para a API:', JSON.stringify(game, null, 2));
        const response = await fetch(`${API_BASE_URL}/game/v2`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(game),
        });
        console.log('Status da resposta:', response.status);
        const responseText = await response.text();
        console.log('Corpo da resposta:', responseText);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return JSON.parse(responseText);
      } catch (error) {
        console.error('Erro ao criar jogo:', error);
        throw error;
      }
    },

    // PUT /api/game/v2 - Atualiza um jogo (envia o objeto completo com id no body)
    update: async (game: Game): Promise<Game> => {
      try {
        const response = await fetch(`${API_BASE_URL}/game/v2`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(game), // Envia o objeto todo, incluindo id
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return await response.json();
      } catch (error) {
        console.error(`Erro ao atualizar jogo ${game.id}:`, error);
        throw error;
      }
    },

    // DELETE /api/game/v2/{id} - Deleta um jogo
    delete: async (id: number): Promise<void> => {
      try {
        const response = await fetch(`${API_BASE_URL}/game/v1/${id}`, {
          method: 'DELETE',
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
      } catch (error) {
        console.error(`Erro ao deletar jogo ${id}:`, error);
        throw error;
      }
    },
  },
};
