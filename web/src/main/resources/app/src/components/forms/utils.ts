const EMAIL_PATTERN = /^[a-zA-Z0-9]{3,}@[a-zA-Z0-9]{3,}\.[a-zA-Z0-9]+$/g;
const USERNAME_PATTERN = /^[a-zA-Z0-9!@#$%^&*()_+{}[\]:;<>,.?~|\\/-]{4,32}$/g;
const PASSWORD_PATTERN = /^[a-zA-Z0-9!@#$%^&*()_+{}[\]:;<>,.?~|\\/-]{4,32}$/g;

export { EMAIL_PATTERN, USERNAME_PATTERN, PASSWORD_PATTERN };
