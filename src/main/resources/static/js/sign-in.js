let sigInAttempted = 0

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
  button.disabled = true
  button.innerHTML = '<span class="spinner-border spinner-border-sm"></span> 처리 중...'
  
  if (sigInAttempted > 3) {
    window.showGlobalToast('로그인을 너무 많이 시도했습니다', 3000)
    
    setTimeout(() => {
      sigInAttempted = 0
      
      button.disabled = false
      button.textContent = '로그인'
      window.isSubmitting = false
    }, 5000)
    
    return
  }
  
  const usernameVal = form.username.value.trim()
  const passwordVal = form.password.value.trim()
  
  if (usernameVal.length === 0 || passwordVal.length === 0) {
    
    window.showGlobalToast('유효한 값을 입력해주세요', 3000)
    
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
    
    if (!response.ok) throw new Error()
    
    window.showGlobalToast('로그인 되었습니다', 3000)
    
    await window.sleep(1000)
    
    form.submit()
  } catch (err) {
    
    sigInAttempted++
    
    window.showGlobalToast('아이디 또는 비밀번호가 일치하지 않습니다', 3000)
    
    button.disabled = false
    button.textContent = '로그인'
    window.isSubmitting = false
  }
}

document.querySelector('#sign-in-form')?.addEventListener('submit', onSignInSubmit)
document.querySelector('#sign-in-form')?.addEventListener('keydown', window.preventEnterSubmit)
