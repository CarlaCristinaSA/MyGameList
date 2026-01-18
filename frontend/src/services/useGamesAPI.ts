/**
 * Hook for managing games API requests
 * 
 * Este hook gerencia as chamadas à API de jogos e o estado da aplicação.
 */

import { useState, useCallback } from 'react';
import type { Game, CreateGameInput } from '../types/Game';
import { api } from './api';

export function useGamesAPI() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchGames = useCallback(async (): Promise<Game[]> => {
    setIsLoading(true);
    setError(null);
    try {
      const games = await api.games.getAll();
      return games;
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Erro desconhecido';
      setError(errorMsg);
      return [];
    } finally {
      setIsLoading(false);
    }
  }, []);

  const getGameById = useCallback(async (id: number): Promise<Game | null> => {
    setIsLoading(true);
    setError(null);
    try {
      const game = await api.games.getById(id);
      return game;
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Erro desconhecido';
      setError(errorMsg);
      return null;
    } finally {
      setIsLoading(false);
    }
  }, []);

  const searchGamesByName = useCallback(
    async (name: string): Promise<Game[]> => {
      setIsLoading(true);
      setError(null);
      try {
        const games = await api.games.findByName(name);
        return games;
      } catch (err) {
        const errorMsg = err instanceof Error ? err.message : 'Erro desconhecido';
        setError(errorMsg);
        return [];
      } finally {
        setIsLoading(false);
      }
    },
    []
  );

  const createGame = useCallback(async (gameInput: CreateGameInput): Promise<Game | null> => {
    setIsLoading(true);
    setError(null);
    try {
      // Não envia o campo id - será gerado automaticamente no backend
      const newGame = await api.games.create(gameInput);
      return newGame;
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Erro desconhecido';
      setError(errorMsg);
      return null;
    } finally {
      setIsLoading(false);
    }
  }, []);

  const updateGame = useCallback(async (game: Game): Promise<Game | null> => {
    setIsLoading(true);
    setError(null);
    try {
      // PUT envia o objeto completo com id no body
      const updatedGame = await api.games.update(game);
      return updatedGame;
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Erro desconhecido';
      setError(errorMsg);
      return null;
    } finally {
      setIsLoading(false);
    }
  }, []);

  const deleteGame = useCallback(async (id: number): Promise<boolean> => {
    setIsLoading(true);
    setError(null);
    try {
      await api.games.delete(id);
      return true;
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Erro desconhecido';
      setError(errorMsg);
      return false;
    } finally {
      setIsLoading(false);
    }
  }, []);

  return {
    isLoading,
    error,
    fetchGames,
    getGameById,
    searchGamesByName,
    createGame,
    updateGame,
    deleteGame,
  };
}
