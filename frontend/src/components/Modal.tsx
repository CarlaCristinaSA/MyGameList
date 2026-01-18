import { useEffect } from 'react';
import { createPortal } from 'react-dom';
import './Modal.css';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
  size?: 'small' | 'medium' | 'large';
}

export function Modal({ isOpen, onClose, title, children, size = 'medium' }: ModalProps) {
  useEffect(() => {
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        onClose();
      }
    };

    if (isOpen) {
      document.addEventListener('keydown', handleEscape);
      document.body.style.overflow = 'hidden';
    }

    return () => {
      document.removeEventListener('keydown', handleEscape);
      document.body.style.overflow = 'unset';
    };
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return createPortal(
    <div className="modal-overlay" onClick={onClose}>
      <div 
        className={`modal-content modal-${size}`} 
        onClick={(e) => e.stopPropagation()}
      >
        <div className="modal-header">
          <h2 className="modal-title">{title}</h2>
          <button className="modal-close" onClick={onClose} title="Fechar">
            <span className="material-symbols-outlined">close</span>
          </button>
        </div>
        <div className="modal-body">
          {children}
        </div>
      </div>
    </div>,
    document.body
  );
}
