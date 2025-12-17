document.addEventListener('DOMContentLoaded', function() {
    const dropZone = document.getElementById('dropZone');
    const fileInput = document.getElementById('fileInput');
    const dropZoneText = document.querySelector('.drop-zone-text');

    // Prevenir comportamiento por defecto del navegador
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropZone.addEventListener(eventName, preventDefaults, false);
    });

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    // Resaltar drop zone cuando se arrastra un archivo sobre ella
    ['dragenter', 'dragover'].forEach(eventName => {
        dropZone.addEventListener(eventName, highlight, false);
    });

    ['dragleave', 'drop'].forEach(eventName => {
        dropZone.addEventListener(eventName, unhighlight, false);
    });

    function highlight(e) {
        dropZone.classList.add('drop-zone-hover');
    }

    function unhighlight(e) {
        dropZone.classList.remove('drop-zone-hover');
    }

    // Manejar archivo soltado
    dropZone.addEventListener('drop', handleDrop, false);

    function handleDrop(e) {
        const dt = e.dataTransfer;
        const files = dt.files;

        if (files.length > 0) {
            fileInput.files = files;
            updateFileName(files[0].name);
        }
    }

    // Manejar selección de archivo mediante el botón
    fileInput.addEventListener('change', function() {
        if (this.files.length > 0) {
            updateFileName(this.files[0].name);
        }
    });

    function updateFileName(fileName) {
        dropZoneText.textContent = `Archivo seleccionado: ${fileName}`;
    }
});
