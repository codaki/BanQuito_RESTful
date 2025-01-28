export function showModal(title, content, isError = false, callback = null) {
  const modalContainer = document.getElementById("custom-modal");
  const modalTitle = document.getElementById("modal-title");
  const modalContent = document.getElementById("modal-content");

  if (!modalContainer || !modalTitle || !modalContent) {
    console.error("Modal elements not found");
    return;
  }

  modalTitle.textContent = title;
  modalContent.textContent = content;

  // Add error styling if needed
  if (isError) {
    modalTitle.classList.add('error-title');
    modalContent.classList.add('error-content');
  } else {
    modalTitle.classList.remove('error-title');
    modalContent.classList.remove('error-content');
  }

  modalContainer.classList.remove("hidden");

  const modalOkButton = document.getElementById("modal-ok-button");
  if (modalOkButton) {
    // Remove any existing event listeners by cloning
    const newButton = modalOkButton.cloneNode(true);
    modalOkButton.parentNode.replaceChild(newButton, modalOkButton);

    // Add the new event listener
    newButton.addEventListener("click", () => {
      closeModal();
      if (callback && typeof callback === 'function') {
        callback();
      }
    });
  }
}

export function closeModal() {
  const modalContainer = document.getElementById("custom-modal");
  if (modalContainer) {
    modalContainer.classList.add("hidden");
  }
}

// Make closeModal available globally for HTML onclick handlers
document.addEventListener("DOMContentLoaded", () => {
  window.closeModal = closeModal;
  console.log("Modal script loaded");
  // Add modal HTML if it doesn't exist
  if (!document.getElementById("custom-modal")) {
    const modalHTML = `
      <div id="custom-modal" class="modal hidden">
        <div class="modal-content">
          <h2 id="modal-title"></h2>
          <p id="modal-content"></p>
          <button id="modal-ok-button" class="btn-primary">OK</button>
        </div>
      </div>
    `;
    document.body.insertAdjacentHTML('beforeend', modalHTML);
  }
});