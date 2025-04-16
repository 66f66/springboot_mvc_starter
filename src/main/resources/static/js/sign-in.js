/**
 * @param e {SubmitEvent}
 * @returns {Promise<void>}
 */
async function onSignInSubmit(e) {
  
  e.preventDefault()
  
  if (window.isSubmitting) return
  window.isSubmitting = true
  
  const form = e.target
  
  const button = form.querySelector('button[type="submit"]')
  const feedback = form.querySelector('#feedback')
  
  button.disabled = true
  button.innerHTML = '<span class="spinner-border spinner-border-sm"></span> 처리 중...'
  
  const usernameVal = form.username.value.trim()
  const passwordVal = form.password.value.trim()
  
  if (usernameVal.length === 0 || passwordVal.length === 0) {
    
    feedback.textContent = '유효한 값을 입력해주세요.'
    feedback.classList.remove('d-none')
    button.disabled = false
    button.textContent = '로그인'
    window.isSubmitting = false
    
    return
  }
  
  const body = {
    username: usernameVal,
    password: passwordVal,
  }
  
  try {
    
    const response = await fetch('/auth/sign-in/pre-process', {
      method: 'POST',
      headers: window.customJsonHeaders,
      body: JSON.stringify(body),
    })
    
    if (!response.ok) {
      
      throw new Error()
    }
    
    form.submit()
  } catch (err) {
    
    feedback.textContent = '아이디 또는 비밀번호가 일치하지 않습니다.'
    feedback.classList.remove('d-none')
    button.disabled = false
    button.textContent = '로그인'
    window.isSubmitting = false
  }
}

document.querySelector('#sign-in-form')?.addEventListener('submit', onSignInSubmit)
document.querySelector('#sign-in-form')?.addEventListener('keydown', window.preventEnterSubmit)
