function run() {
  /**
   * @param {HTMLElement} element
   */
  let toggleField = (element, forceReadonly) => {
    if (element.value || forceReadonly) {
      element.setAttribute("readonly", "");
    } else {
      element.removeAttribute("readonly");
    }
  };

  let clearFields = () => {
    let street = document.getElementById("street");
    street.value = "";
    toggleField(street, true);

    let district = document.getElementById("district");
    district.value = "";
    toggleField(district, true);

    let city = document.getElementById("city");
    city.value = "";
    toggleField(city, true);

    let state = document.getElementById("state");
    state.value = "";
    toggleField(state, true);
  };

  let showError = () => {
    let err = document.createElement("div");
    err.classList.add("person-form__error");
    err.textContent =
      "O CEP informado não é válido. Por favor, digite um CEP válido.";

    let parent = document.querySelector(".person-form__footer");
    parent.prepend(err);

    clearFields();
  };

  let submitButton = document.getElementById("submit-btn");

  let cepInput = document.getElementById("cep");
  cepInput.addEventListener("input", function () {
    submitButton.setAttribute("disabled", "");
  });

  let cepButton = document.getElementById("cep-btn");
  cepButton.onclick = () => {
    // pega o CEP e extrai apenas os dígitos
    let cep = document.getElementById("cep").value.replace(/\D/g, "");

    let req = new XMLHttpRequest();
    let url = `https://viacep.com.br/ws/${cep}/json/`;

    req.responseType = "json";
    req.open("GET", url, true);

    req.onloadstart = () => {
      cepButton.setAttribute("disabled", "");
      let err = document.querySelector(".person-form__error");
      if (err) err.remove();
    };
    req.onloadend = () => {
      cepButton.removeAttribute("disabled");
    };
    req.onload = () => {
      let json = req.response;

      let isReturnEmpty =
        !json["logradouro"] &&
        !json["bairro"] &&
        !json["localidade"] &&
        !json["uf"];
      if (isReturnEmpty) {
        return showError();
      }

      submitButton.removeAttribute("disabled");

      let street = document.getElementById("street");
      if (json["logradouro"]) street.value = json["logradouro"];
      toggleField(street);

      let district = document.getElementById("district");
      if (json["bairro"]) district.value = json["bairro"];
      toggleField(district);

      let city = document.getElementById("city");
      if (json["localidade"]) city.value = json["localidade"];
      toggleField(city);

      let state = document.getElementById("state");
      if (json["uf"]) state.value = json["uf"];
      toggleField(state);
    };
    req.onerror = showError;

    req.send();
  };
}
run();
