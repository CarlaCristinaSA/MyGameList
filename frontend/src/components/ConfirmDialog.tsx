import { Modal } from './Modal';
import './ConfirmDialog.css';

interface ConfirmDialogProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  type?: 'danger' | 'warning' | 'info';
  isLoading?: boolean;
}

export function ConfirmDialog({
  isOpen,
  onClose,
  onConfirm,
  title,
  message,
  confirmText = 'Confirmar',
  cancelText = 'Cancelar',
  type = 'danger',
  isLoading = false,
}: ConfirmDialogProps) {
  const handleConfirm = () => {
    onConfirm();
  };

  const getIcon = () => {
    switch (type) {
      case 'danger':
        return 'warning';
      case 'warning':
        return 'error';
      case 'info':
        return 'info';
      default:
        return 'help';
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={title} size="small">
      <div className={`confirm-dialog confirm-${type}`}>
        <div className="confirm-icon-container">
          <span className="material-symbols-outlined confirm-icon">{getIcon()}</span>
        </div>
        <p className="confirm-message">{message}</p>
        <div className="confirm-actions">
          <button
            className="btn-cancel"
            onClick={onClose}
            disabled={isLoading}
          >
            <span className="material-symbols-outlined">close</span>
            {cancelText}
          </button>
          <button
            className={`btn-confirm btn-confirm-${type}`}
            onClick={handleConfirm}
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <span className="loading-spinner-small"></span>
                Processando...
              </>
            ) : (
              <>
                <span className="material-symbols-outlined">check</span>
                {confirmText}
              </>
            )}
          </button>
        </div>
      </div>
    </Modal>
  );
}
