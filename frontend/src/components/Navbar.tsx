import './Navbar.css';

interface NavbarProps {
  activeTab: 'home' | 'cadastro' | 'catalogo';
  onTabChange: (tab: 'home' | 'cadastro' | 'catalogo') => void;
}

export function Navbar({ activeTab, onTabChange }: NavbarProps) {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="navbar-brand" onClick={() => onTabChange('home')}>
          <span className="navbar-title">MyGameList</span>
        </div>
        
        <div className="navbar-menu">
          <button 
            className={`navbar-item ${activeTab === 'home' ? 'active' : ''}`}
            onClick={() => onTabChange('home')}
          >
            Início
          </button>
          <button 
            className={`navbar-item ${activeTab === 'cadastro' ? 'active' : ''}`}
            onClick={() => onTabChange('cadastro')}
          >
            Cadastrar
          </button>
          <button 
            className={`navbar-item ${activeTab === 'catalogo' ? 'active' : ''}`}
            onClick={() => onTabChange('catalogo')}
          >
            Catálogo
          </button>
        </div>
      </div>
    </nav>
  );
}
