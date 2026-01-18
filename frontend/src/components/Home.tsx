import './Home.css';

interface HomeProps {
  stats: {
    total: number;
    finished: number;
    avgRating: string;
  };
  onNavigate: (tab: 'cadastro' | 'catalogo') => void;
}

export function Home({ stats, onNavigate }: HomeProps) {
  return (
    <div className="home">
      <section className="hero">
        <div className="hero-content">
          <div className="hero-icon">🎮</div>
          <h1 className="hero-title">MyGameList</h1>
          <p className="hero-subtitle">
            Seu gerenciador pessoal de jogos
          </p>
          <p className="hero-description">
            Organize, avalie e acompanhe sua coleção de jogos de forma simples e intuitiva.
            Nunca mais esqueça quais jogos você jogou!
          </p>
          
          <div className="hero-actions">
            <button 
              className="btn-primary"
              onClick={() => onNavigate('cadastro')}
            >
              <span className="material-symbols-outlined">add_circle</span>
              Cadastrar Jogo
            </button>
            <button 
              className="btn-secondary"
              onClick={() => onNavigate('catalogo')}
            >
              <span className="material-symbols-outlined">book</span>
              Ver Catálogo
            </button>
          </div>
        </div>
      </section>

      <section className="stats-section">
        <h2 className="section-title">
          <span className="material-symbols-outlined">analytics</span>
          Estatísticas da Coleção
        </h2>
        
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-icon">
              <span className="material-symbols-outlined">videogame_asset</span>
            </div>
            <div className="stat-value">{stats.total}</div>
            <div className="stat-label">Total de Jogos</div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon">
              <span className="material-symbols-outlined">check_circle</span>
            </div>
            <div className="stat-value">{stats.finished}</div>
            <div className="stat-label">Concluídos</div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon">
              <span className="material-symbols-outlined">pending</span>
            </div>
            <div className="stat-value">{stats.total - stats.finished}</div>
            <div className="stat-label">Pendentes</div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon">
              <span className="material-symbols-outlined">star</span>
            </div>
            <div className="stat-value">{stats.avgRating}</div>
            <div className="stat-label">Avaliação Média</div>
          </div>
        </div>

        <div className="progress-card">
          <div className="progress-header">
            <span className="progress-title">Progresso de Conclusão</span>
            <span className="progress-percentage">
              {stats.total > 0 ? Math.round((stats.finished / stats.total) * 100) : 0}%
            </span>
          </div>
          <div className="progress-bar">
            <div 
              className="progress-fill" 
              style={{ 
                width: `${stats.total > 0 ? (stats.finished / stats.total) * 100 : 0}%` 
              }}
            ></div>
          </div>
          <div className="progress-info">
            {stats.finished} de {stats.total} jogos completados
          </div>
        </div>
      </section>

      <section className="features-section">
        <h2 className="section-title">Funcionalidades</h2>
        <div className="features-grid">
          <div className="feature-card">
            <div className="feature-icon">
              <span className="material-symbols-outlined">edit_note</span>
            </div>
            <h3 className="feature-title">Cadastre Seus Jogos</h3>
            <p className="feature-description">
              Adicione jogos com informações detalhadas: título, descrição, data de lançamento e muito mais.
            </p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">
              <span className="material-symbols-outlined">grade</span>
            </div>
            <h3 className="feature-title">Avalie e Classifique</h3>
            <p className="feature-description">
              Dê notas de 0 a 5 estrelas para seus jogos e marque quais você já terminou.
            </p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">
              <span className="material-symbols-outlined">search</span>
            </div>
            <h3 className="feature-title">Pesquise e Filtre</h3>
            <p className="feature-description">
              Encontre rapidamente qualquer jogo da sua coleção com busca inteligente.
            </p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">
              <span className="material-symbols-outlined">bar_chart</span>
            </div>
            <h3 className="feature-title">Acompanhe Estatísticas</h3>
            <p className="feature-description">
              Veja quantos jogos você tem, quantos completou e sua média de avaliações.
            </p>
          </div>
        </div>
      </section>

      <section className="cta-section">
        <h2 className="cta-title">Pronto para começar?</h2>
        <p className="cta-description">
          Cadastre seu primeiro jogo e comece a organizar sua coleção agora mesmo!
        </p>
        <button 
          className="btn-cta"
          onClick={() => onNavigate('cadastro')}
        >
          Cadastrar Primeiro Jogo
        </button>
      </section>
    </div>
  );
}
