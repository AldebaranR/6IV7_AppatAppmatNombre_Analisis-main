document.addEventListener("DOMContentLoaded", function () {
    const links = document.querySelectorAll(".validate-link");

    links.forEach(link => {
        link.addEventListener("click", function (event) {
            const confirmed = confirm("¿Estás seguro de que deseas abrir esta práctica?");
            if (!confirmed) {
                event.preventDefault();
            }
        });
    });
});
