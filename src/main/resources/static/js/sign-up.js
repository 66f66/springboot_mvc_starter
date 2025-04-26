/**
 * @param e {SubmitEvent}
 */
async function onSignUpSubmit(e) {
  
  e.preventDefault()
  
  if (window.isSubmitting) return
  window.isSubmitting = true
  
  const form = e.target
  let isValid = true
  
  const submitButton = form.querySelector('button[type="submit"]')
  submitButton.disabled = true
  submitButton.innerHTML = '<span class="spinner-border spinner-border-sm"></span> 처리 중...'
  
  // validate username
  const username = form.querySelector('input[name="username"]')
  const usernameVal = username.value.trim()
  const usernameRegex = /^[a-zA-Z0-9!@#$%^&*()_+\-=\[\]{};':",./<>?]{2,12}$/
  const usernameFeedback = form.querySelector('#username-invalid-feedback')
  
  if (!usernameRegex.test(usernameVal)) {
    
    username.classList.add('is-invalid')
    usernameFeedback.textContent = '아이디는 2~12자의 영문, 숫자, 특수문자만 사용가능합니다.'
    isValid = false
  } else {
    
    username.classList.remove('is-invalid')
    usernameFeedback.textContent = ''
  }
  
  // validate nickname
  const nickname = form.querySelector('input[name="nickname"]')
  const nicknameVal = nickname.value.trim()
  const nicknameRegex = window.nicknameRegex
  const nicknameFeedback = form.querySelector('#nickname-invalid-feedback')
  
  if (!nicknameRegex.test(nicknameVal)) {
    
    nickname.classList.add('is-invalid')
    nicknameFeedback.textContent = '닉네임은 2~10자의 한글, 영문, 숫자만 사용 가능합니다.'
    isValid = false
  } else {
    
    nickname.classList.remove('is-invalid')
    nicknameFeedback.textContent = ''
  }
  
  // validate password
  const password = form.querySelector('input[name="password"]')
  const passwordVal = password.value.trim()
  const passwordRegex = window.passwordRegex
  const passwordFeedback = form.querySelector('#password-invalid-feedback')
  
  if (!passwordRegex.test(passwordVal)) {
    
    password.classList.add('is-invalid')
    passwordFeedback.textContent = '비밀번호는 8~12자이며, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.'
    isValid = false
  } else {
    
    password.classList.remove('is-invalid')
    passwordFeedback.textContent = ''
  }
  
  // validate password check
  const passwordCheck = form.querySelector('input[name="passwordCheck"]')
  const passwordCheckVal = passwordCheck.value.trim()
  const passwordCheckFeedback = form.querySelector('#password-check-invalid-feedback')
  
  if (passwordCheckVal.length === 0) {
    
    passwordCheck.classList.add('is-invalid')
    passwordCheckFeedback.textContent = '비밀번호를 다시 입력해주세요.'
    isValid = false
  } else if (passwordVal !== passwordCheckVal) {
    
    passwordCheck.classList.add('is-invalid')
    passwordCheckFeedback.textContent = '비밀번호 확인과 비밀번호가 일치하지 않습니다.'
    isValid = false
  } else {
    
    passwordCheck.classList.remove('is-invalid')
    passwordCheckFeedback.textContent = ''
  }
  
  // exists by username
  if (isValid) {
    
    try {
      const response = await fetch(`/auth/exists-by-username?username=${usernameVal}`, {
        headers: customJsonHeaders,
        method: 'GET',
      })
      
      if (!response.ok) throw new Error()
      
      const body = await response.json()
      
      if (body.existsByUsername) throw new Error()
    } catch (err) {
      
      username.classList.add('is-invalid')
      usernameFeedback.textContent = '이미 사용중인 아이디입니다.'
      isValid = false
    }
  }
  
  if (!isValid) {
    
    submitButton.disabled = false
    submitButton.textContent = '회원가입'
    window.isSubmitting = false
    
    return
  }
  
  try {
    const body = {
      username: usernameVal,
      nickname: nicknameVal,
      password: passwordVal,
      passwordCheck: passwordCheckVal,
    }
    
    const response = await fetch('/auth/sign-up', {
      method: 'post',
      headers: customJsonHeaders,
      body: JSON.stringify(body),
    })
    
    if (!response.ok) throw new Error()
    
    window.showGlobalToast('회원가입 되었습니다', 3000)
    
    await window.sleep(1000)
    
    window.history.back()
  } catch (e) {
    
    window.showGlobalToast('현재 회원가입 할 수 없습니다', 3000)
    
    submitButton.disabled = false
    submitButton.textContent = '회원가입'
    window.isSubmitting = false
  }
}

document.querySelector('#sign-up-form')?.addEventListener('submit', onSignUpSubmit)
document.querySelector('#sign-up-form')?.addEventListener('keydown', window.preventEnterSubmit)
