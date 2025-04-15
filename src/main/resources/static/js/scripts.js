const springSecurityCsrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content')
const springSecurityCsrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content')

const commonJsonHeaders = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
}

const springSecurityHeaders = {
  [springSecurityCsrfHeader]: springSecurityCsrfToken,
}

window.customHeaders = {
  ...commonJsonHeaders,
  ...springSecurityHeaders,
}

let isSubmitting = false

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
