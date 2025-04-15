/**
 * @param e {SubmitEvent}
 * @returns {Promise<void>}
 */
async function articleSubmit(e) {
  
  e.preventDefault()
  
  if (window.isSubmitting) return
  window.isSubmitting = true
  
  const form = e.target
  let isValid = true
  
  const submitButton = form.querySelector('button[type="submit"]')
  submitButton.disabled = true
  submitButton.innerHTML = '<span class="spinner-border spinner-border-sm"></span> 처리 중...'
  
  // validate title
  const title = form.title
  const titleVal = title.value
  const titleFeedback = title.nextElementSibling
  
  if (titleVal.trim().length === 0) {
    
    title.classList.add('is-invalid')
    titleFeedback.textContent = '제목은 공백일 수 없습니다.'
    isValid = false
  } else if (titleVal.length < 2 || titleVal.length > 100) {
    
    title.classList.add('is-invalid')
    titleFeedback.textContent = '제목은 2자 이상 100자 이하입니다.'
    isValid = false
  } else {
    
    title.classList.remove('is-invalid')
    titleFeedback.textContent = ''
  }
  
  // validate content
  const content = form.content
  const contentVal = content.value
  const contentFeedback = content.closest('.mb-3').querySelector('.invalid-feedback')
  
  if (contentVal.trim().length === 0) {
    
    content.classList.add('is-invalid')
    contentFeedback.textContent = '내용은 공백일 수 없습니다.'
    isValid = false
  } else if (contentVal.length < 10 || contentVal.length > 2000) {
    
    content.classList.add('is-invalid')
    contentFeedback.textContent = '내용은 10자 이상 2000자 이하입니다.'
    isValid = false
  } else {
    
    content.classList.remove('is-invalid')
    contentFeedback.textContent = ''
  }
  
  // category id
  const articleCategoryId = form.articleCategoryId
  const articleCategoryIdVal = articleCategoryId.value
  
  if (!isValid) {
    
    submitButton.disabled = false
    submitButton.innerHTML = '저장'
    window.isSubmitting = false
    
    return
  }
  
  try {
    
    const body = {
      title: titleVal,
      content: contentVal,
      articleCategoryId: articleCategoryIdVal,
      id: form.articleId?.value,
    }
    
    const response = await fetch(form.action, {
      method: 'POST',
      headers: window.customHeaders,
      body: JSON.stringify(body),
    })
    
    const data = await response.json()
    
    if (!response.ok) {
      
      if (response.status === 400 && body.errors) {
        
        alert(window.formattedErrors(body.errors))
      }
      
      throw new Error()
    }
    
    window.location.replace(`/article/${data.id}`)
  } catch (err) {
    
    alert('게시물을 저장하지 못했습니다.')
    
    submitButton.disabled = false
    submitButton.innerHTML = '저장'
    window.isSubmitting = false
  }
}

document.querySelector('#article-create-form, #article-update-form')?.addEventListener('submit', articleSubmit)
document.querySelector('#article-create-form, #article-update-form')?.addEventListener('keydown', window.preventEnterSubmit)
