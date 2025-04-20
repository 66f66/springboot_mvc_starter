const springSecurityCsrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content')
const springSecurityCsrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content')
const springSecurityHeaders = {
  [springSecurityCsrfHeader]: springSecurityCsrfToken,
}

const commonJsonHeaders = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
}

// custom headers
window.customJsonHeaders = {
  ...commonJsonHeaders,
  ...springSecurityHeaders,
}
window.customHeaders = {
  ...springSecurityHeaders,
}

// sleep
window.sleep = function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms))
}

// isSubmitting
let isSubmitting = false
window.isSubmitting = isSubmitting

/**
 * @param e {KeyboardEvent}
 */
window.preventEnterSubmit = function preventEnterSubmit(e) {
  if (e.key === 'Enter' && isSubmitting) {
    e.preventDefault()
  }
}

/**
 * @param errors {string[]}
 * @returns {string}
 */
window.formattedErrors = function formattedErrors(errors) {
  
  return errors
    .map(error => `• ${error}`)
    .join('\n')
}

// regexes
window.nicknameRegex = /^[가-힣a-zA-Z0-9]{2,10}$/
window.passwordRegex = /^[A-Za-z0-9!@#$%^&*()_+\-=\[\]{};':",./<>?]{8,12}$/
