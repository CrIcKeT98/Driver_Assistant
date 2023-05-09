$(document).ready(function(){
    $('.modal').modal();
    $(".dropdown-trigger").dropdown();
 });

//Prohibit line break
$('textarea').on('keydown', function (e) {
    if (e.keyCode === 13) {
        e.preventDefault();
    }

    const lMaxLength = 18;
    var input = this.value;
    if (input.length > lMaxLength) {
        // Ограничиваем количество введенных символов
        this.value = input.slice(0, lMaxLength);
    }
});

//Navigation
function hideBackArrow(){
    const lButtonLeft = document.querySelector('#nav_back_arrow');
    lButtonLeft.classList.add('hide_content');
}

function showBackArrow(){
    const lButtonLeft = document.querySelector('#nav_back_arrow');
    lButtonLeft.classList.remove('hide_content');
}
