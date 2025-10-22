const form = document.getElementById("signup-form");
const lastName = document.getElementById("lastName");
const firstName = document.getElementById("firstName");
const email = document.getElementById("email");
const password = document.getElementById("password");
const toast = document.getElementById("toast");
const googleBtn = document.getElementById("google-btn");
const BACKEND_URL = "http://localhost:8080";


form.addEventListener("submit", (e) => {
  e.preventDefault();
  const valid =
    checkName(lastName) &
    checkName(firstName) &
    checkEmail() &
    checkPassword();

  if (valid) {
    showToast();
    clearFields();
    [lastName, firstName, email, password].forEach(
      (i) => (i.style.borderColor = "#ccc")
    );
  }
});

googleBtn.addEventListener("click", (e) => {
      e.preventDefault();
//      window.location.href = `${BACKEND_URL}/api/v1/auth/google`;
      window.location.href = "http://localhost:8080/oauth2/authorization/google";
});

function showError(input, message) {
  const parent = input.parentElement;
  const error = parent.querySelector(".error");
  error.textContent = message;
  input.style.borderColor = "red";
}

function showSuccess(input) {
  const parent = input.parentElement;
  const error = parent.querySelector(".error");
  error.textContent = "";
  input.style.borderColor = "green";
}

function checkName(input) {
  if (input.value.trim() === "") {
    showError(input, "This field is required.");
    return false;
  }
  showSuccess(input);
  return true;
}

function checkEmail() {
  const value = email.value.trim();
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (value === "") {
    showError(email, "This field is required.");
    return false;
  } else if (!regex.test(value)) {
    showError(email, "Please enter a valid email address.");
    return false;
  }
  showSuccess(email);
  return true;
}

function checkPassword() {
  const value = password.value.trim();
  if (value.length < 8) {
    showError(password, "Please enter at least 8 caracters");
    return false;
  }
  showSuccess(password);
  return true;
}

[lastName, firstName, email, password].forEach((input) => {
  input.addEventListener("input", () => {
    if (input === email) checkEmail();
    else if (input === password) checkPassword();
    else checkName(input);
  });
});

const clearFields = () => {
    firstName.value = "";
    lastName.value = "";
    email.value = "";
    password.value = "";
}

function showToast() {
  const h4 = `
        <h4>
            <img src="./assets/icon-success-check.svg" alt="">
            Message Sent!
        </h4>
    `
    const p = `
        <p>Thanks for completing the form. We'll be in touch soon!</p>
    `
    toast.innerHTML = h4 + p;

    toast.classList.add('show');
    setTimeout(() => {
        toast.classList.remove('show')
    }, 2000);
}

