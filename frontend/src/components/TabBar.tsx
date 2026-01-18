import './TabBar.css';

interface TabBarProps {
  activeTab: 'cadastro' | 'catalogo';
  onTabChange: (tab: 'cadastro' | 'catalogo') => void;
}

export function TabBar({ activeTab, onTabChange }: TabBarProps) {
  return (
    <div className="tab-bar">
      <button
        className={`tab-button ${activeTab === 'cadastro' ? 'active' : ''}`}
        onClick={() => onTabChange('cadastro')}
      >
        Cadastrar
      </button>
      <button
        className={`tab-button ${activeTab === 'catalogo' ? 'active' : ''}`}
        onClick={() => onTabChange('catalogo')}
      >
        Catálogo
      </button>
    </div>
  );
}
