//Create new button in list and adding custom attribute "button_id" for search in DB
function createEmergencyListButton(obj){
    const lButtonList = document.querySelector('.emergency-button-list');
    
    let lButton = document.createElement('a');
    lButton.className = "waves-effect waves-light btn";
    lButton.innerHTML = obj.text;
//    l_button.href = "action_list.html" + "?id=" + obj.id;
    lButton.setAttribute("button_id", obj.button_id);
    lButton.addEventListener('click', emergencyButtonListListener);

    lButtonList.append(lButton);
}

function emergencyButtonListListener (e) {
    Android.emergencyButtonListListener(e.srcElement.getAttribute("button_id"), e.srcElement.textContent);
}