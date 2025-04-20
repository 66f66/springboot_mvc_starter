let isNewImage = false
let oldImageUrl = ''
const image = document.getElementById('image')

function setOldImageUrl() {
  
  if (oldImageUrl !== '') {
    
    image.src = oldImageUrl
  }
}

/**
 * @param e {InputEvent}
 */
function onFileInput(e) {
  
  isNewImage = false
  
  const fileInput = e.target
  const file = fileInput.files[0]
  
  if (!file) return
  
  const allowedTypes = ['image/jpeg', 'image/png']
  
  if (!allowedTypes.includes(file.type)) {
    
    alert('JPEG 또는 PNG 이미지 파일만 업로드 가능합니다.')
    fileInput.value = ''
    setOldImageUrl()
    
    return
  }
  
  const maxSize = 1024 * 1024
  if (file.size > maxSize) {
    
    alert('파일 크기는 1MB를 초과할 수 없습니다.')
    fileInput.value = ''
    setOldImageUrl()
    
    return
  }
  
  isNewImage = true
  
  if (!oldImageUrl) {
    oldImageUrl = image.src
  }
  
  if (image.src.startsWith('blob:')) {
    URL.revokeObjectURL(image.src)
  }
  image.src = URL.createObjectURL(file)
}

document.querySelector('#user-file-input').addEventListener('change', onFileInput)

const navbarUserImage = document.getElementById('navbar-user-image')

/**
 * @param e {SubmitEvent}
 */
async function onUserUpdate(e) {
  
  e.preventDefault()
  if (window.isSubmitting) return
  window.isSubmitting = true
  
  const form = e.target
  let isValid = true
  
  const submitButton = form.querySelector('button[type="submit"]')
  submitButton.disabled = true
  submitButton.innerHTML = '<span class="spinner-border spinner-border-sm"></span> 처리 중...'
  
  if (isNewImage) {
    
    try {
      
      const formData = new FormData()
      formData.append('file', form.file.files[0])
      
      const response = await fetch('/user/update/image', {
        method: 'POST',
        headers: window.customHeaders,
        body: formData,
      })
      
      if (!response.ok) throw new Error()
      
      window.showGlobalToast('이미지를 저장했습니다', 3000)
      
      const data = await response.json()
      navbarUserImage.src = data?.imageUrl
    } catch (err) {
      
      window.showGlobalToast('이미지를 저장하지 못했습니다', 3000)
    } finally {
      
      isNewImage = false
      form.file.value = ''
    }
  }
  
  // nickname
  const nickname = form.nickname
  const nicknameVal = nickname.value
  const nicknameRegex = window.nicknameRegex
  let isNicknameTouched = false
  
  if (nicknameVal.length !== 0) {
    if (!nicknameRegex.test(nicknameVal)) {
      
      isValid = false
      alert('유효한 닉네임이 아닙니다.')
      
    } else {
      
      isNicknameTouched = true
    }
  }
  
  // password
  const oldPassword = form.oldPassword
  let oldPasswordVal = oldPassword.value
  const newPassword = form.newPassword
  const newPasswordVal = newPassword.value
  const passwordRegex = window.passwordRegex
  let isPasswordTouched = false
  
  if (newPasswordVal.length !== 0) {
    
    if (oldPasswordVal.length === 0) {
      
      isValid = false
      alert('비밀번호를 변경하려면 현재 비밀번호를 입력해야 합니다.')
    } else if (!passwordRegex.test(newPasswordVal)
      || !passwordRegex.test(oldPasswordVal)) {
      
      isValid = false
      alert('유효한 비밀번호가 아닙니다.')
    } else {
      
      isPasswordTouched = true
    }
  }
  
  try {
    
    if (!isNicknameTouched && !isPasswordTouched) throw new Error()
    
    if (!isValid) throw new Error()
    
    const body = {}
    if (isNicknameTouched) {
      body.nickname = nicknameVal
    }
    if (isPasswordTouched) {
      body.oldPassword = oldPasswordVal
      body.newPassword = newPasswordVal
    }
    
    const response = await fetch('/user/update', {
      method: 'POST',
      headers: window.customJsonHeaders,
      body: JSON.stringify(body),
    })
    
    if (!response.ok) {
      
      const data = await response.json()
      
      alert(data.message)
      
      throw new Error()
    }
    
    const data = await response.json()
    const user = data.user
    
    navbarUserImage.src = user.image.url
    
    window.showGlobalToast('변경사항을 저장했습니다', 3000)
    
    await window.sleep(3000)
    
    window.location.reload()
  } catch (err) {
    
    submitButton.disabled = false
    submitButton.innerHTML = '저장'
    window.isSubmitting = false
  }
}

document.querySelector('#user-update-form').addEventListener('submit', onUserUpdate)
document.querySelector('#user-update-form').addEventListener('keydown', window.preventEnterSubmit)
