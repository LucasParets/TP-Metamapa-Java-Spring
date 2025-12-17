function mostrarFormNuevoCriterio() {
    document.getElementById('nuevo-criterio-form').style.display = 'block';
}

function ocultarFormNuevoCriterio() {
    document.getElementById('nuevo-criterio-form').style.display = 'none';
}

function confirmarEliminacion(form) {
    Swal.fire({
        title: '¡Cuidado!',
        text: '¿Está seguro que desea eliminar el criterio?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Eliminar',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#d33',
        cancelButtonColor: '#8f8f8f',
        focusCancel: true
    }).then((result) => {
        if (result.isConfirmed) {
            form.submit();
        }
    });
}

function actualizarParametrosNuevo(select) {
    const paramsContainer = document.getElementById('params-nuevo');
    paramsContainer.innerHTML = '';
    document.getElementById('categoria-select').style.display = 'none';
    document.getElementById('categoria-select-input').required = false;

    const criterioTipo = select.value;
    
    switch (criterioTipo) {
        case 'fecha_hecho_desde':
            agregarCampoFecha(paramsContainer, 'fecha_hecho');
            break;
        case 'fecha_hecho_hasta':
            agregarCampoFecha(paramsContainer, 'fecha_hecho');
            break;
        case 'fecha_hecho_exacta':
            agregarCampoFechaSinHora(paramsContainer, 'fecha_hecho');
            break;
        case 'fecha_carga_desde':
            agregarCampoFecha(paramsContainer, 'fecha_carga');
            break;
        case 'fecha_carga_hasta':
            agregarCampoFecha(paramsContainer, 'fecha_carga');
            break;
        case 'fecha_carga_exacta':
            agregarCampoFechaSinHora(paramsContainer, 'fecha_carga');
            break;
            
        case 'fecha_hecho_intervalo':
            agregarCampoFecha(paramsContainer, 'fecha_inicio');
            agregarCampoFecha(paramsContainer, 'fecha_fin');
            break;
        case 'fecha_carga_intervalo':
            agregarCampoFecha(paramsContainer, 'fecha_inicio');
            agregarCampoFecha(paramsContainer, 'fecha_fin');
            break;
            
        case 'coincide_categoria':
            agregarCampoCategoria();
            break;

        case 'contiene_etiqueta':
            agregarCampoEtiqueta(paramsContainer, 'etiqueta');
            break;
            
        case 'rango_distancia':
            agregarCampoNumerico(paramsContainer, 'latitud', 'Latitud');
            agregarCampoNumerico(paramsContainer, 'longitud', 'Longitud');
            agregarCampoNumerico(paramsContainer, 'rango_distancia', 'Rango (km)');
            break;
    }
}

function agregarCampoEtiqueta(container, nombre) {
    const div = document.createElement('div');
    div.style.display = 'flex';
    div.style.flexDirection = 'column';
    div.style.gap = '0.5rem';
    div.style.marginBottom = '5px';

    const label = document.createElement('label');
    label.textContent = "Nombre de la etiqueta:";

    const input = document.createElement('input');
    input.type = 'text';
    input.name = `parametros['${nombre}']`;
    input.className = 'parametro-input';
    input.minLength = 3;
    input.maxLength = 25;
    input.required = true;
    input.style.padding = '8px';
    input.style.borderRadius = '4px';
    input.style.border = '1px solid #ddd';

    div.appendChild(label);
    div.appendChild(input);
    container.appendChild(div);
}

function agregarCampoFechaSinHora(container, nombre) {
    const div = document.createElement('div');
    div.style.display = 'flex';
    div.style.flexDirection = 'column';
    div.style.gap = '0.5rem';
    div.style.marginBottom = '5px';

    const label = document.createElement('label');
    label.textContent = formatearNombre(nombre);

    const input = document.createElement('input');
    input.type = 'date';
    input.name = `parametros['${nombre}']`;
    input.className = 'parametro-input';
    input.required = true;
    input.min = '1990-01-01';

    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');

    input.max = `${year}-${month}-${day}`;

    input.style.padding = '8px';
    input.style.borderRadius = '4px';
    input.style.border = '1px solid #ddd';

    div.appendChild(label);
    div.appendChild(input);
    container.appendChild(div);
}

function agregarCampoFecha(container, nombre) {
    const div = document.createElement('div');
    div.style.display = 'flex';
    div.style.flexDirection = 'column';
    div.style.gap = '0.5rem';
    div.style.marginBottom = '5px';
    
    const label = document.createElement('label');
    label.textContent = formatearNombre(nombre);
    
    const input = document.createElement('input');
    input.type = 'datetime-local';
    input.name = `parametros['${nombre}']`;
    input.className = 'parametro-input';
    input.required = true;
    input.min = '1990-01-01T00:00';

    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');

    input.max = `${year}-${month}-${day}T${hours}:${minutes}`;

    input.style.padding = '8px';
    input.style.borderRadius = '4px';
    input.style.border = '1px solid #ddd';
    
    div.appendChild(label);
    div.appendChild(input);
    container.appendChild(div);
}


function agregarCampoCategoria() {
    document.getElementById('categoria-select').style.display = 'flex';
    document.getElementById('categoria-select-input').required = true;
}

function agregarCampoNumerico(container, nombre, label) {
    const div = document.createElement('div');
    div.style.display = 'flex';
    div.style.flexDirection = 'column';
    div.style.gap = '0.5rem';
    div.style.marginBottom = '5px';
    
    const labelEl = document.createElement('label');
    labelEl.textContent = label;
    
    const input = document.createElement('input');
    input.type = 'number';
    input.step = nombre === 'rango_distancia' ? '0.1' : '0.000001';
    input.name = `parametros['${nombre}']`;
    input.className = 'parametro-input';
    input.required = true;
    input.style.padding = '8px';
    input.style.borderRadius = '4px';
    input.style.border = '1px solid #ddd';
    
    div.appendChild(labelEl);
    div.appendChild(input);
    container.appendChild(div);
}

function formatearNombre(nombre) {
    // Convierte snake_case a formato legible
    return nombre.split('_').map(word => 
        word.charAt(0).toUpperCase() + word.slice(1)
    ).join(' ');
}


