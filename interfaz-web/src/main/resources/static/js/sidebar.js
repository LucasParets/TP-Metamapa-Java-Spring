function openCloseSidebar() {
    const sidebarMenu = document.getElementById('sidebar-menu-container');
    const sidebarBtn = document.getElementById('sidebar-btn-container');

    const currentWidth = sidebarMenu.style.width;
    if (!currentWidth || currentWidth === '0px') {
        sidebarMenu.style.width = '250px';
        sidebarBtn.style.left = '250px';
    } else {
        sidebarMenu.style.width = '0';
        sidebarBtn.style.left = '0';
    }
}
