function confirmarEliminacion(form) {

    Swal.fire({
        title: '¡Cuidado!',
        text: '¿Está seguro que desea eliminar la colección?',
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