/**
 * @param {string} currentFormId
 */
function closeOtherForms(currentFormId) {
  
  document.querySelectorAll('[id^="comment_reply_form_"], [id^="comment_update_form_"]').forEach(form => {
    if (form.id !== currentFormId) {
      form.style.display = 'none'
    }
  })
}

/**
 * @param {number} commentId
 */
function toggleUpdateForm(commentId) {
  
  const formId = 'comment_update_form_' + commentId
  const form = document.getElementById(formId)
  const isOpen = form.style.display === 'block'
  
  form.style.display = isOpen ? 'none' : 'block'
  
  if (!isOpen) {
    
    closeOtherForms(formId)
  }
}

/**
 * @param {number} commentId
 */
function toggleReplyForm(commentId) {
  
  const formId = 'comment_reply_form_' + commentId
  const form = document.getElementById(formId)
  const isOpen = form.style.display === 'block'
  
  form.style.display = isOpen ? 'none' : 'block'
  
  if (!isOpen) {
    
    closeOtherForms(formId)
  }
}

/**
 * @param {SubmitEvent} e
 */
async function onCommentDelete(e) {
  
  e.preventDefault()
  
  if (window.isSubmitting) return
  window.isSubmitting = true
  
  const confirmed = confirm('댓글을 정말 삭제하시겠습니까?')
  if (!confirmed) return
  
  const form = e.target
  
  try {
    
    const commentIdVal = form.commentId?.value
    if (!commentIdVal) throw new Error()
    
    const response = await fetch(`/comment/${commentIdVal}`, {
      method: 'delete',
      headers: window.customJsonHeaders,
    })
    
    if (!response.ok) throw new Error()
    
    window.location.reload()
  } catch (err) {
    
    window.showGlobalToast('댓글을 삭제하지 못했습니다', 3000)
    
    window.isSubmitting = false
  }
}

document.querySelectorAll('.comment-delete-form').forEach(form => {
  
  form.addEventListener('submit', onCommentDelete)
})

/**
 * @param {SubmitEvent} e
 */
async function onCommentSubmit(e) {
  
  e.preventDefault()
  
  if (window.isSubmitting) return
  window.isSubmitting = true
  
  const form = e.target
  let isValid = true
  
  const submitButton = form.querySelector('button[type="submit"]')
  submitButton.disabled = true
  submitButton.innerHTML = '<span class="spinner-border spinner-border-sm"></span> 처리 중...'
  
  // validate content
  const content = form.content
  const contentValue = content.value
  const contentFeedback = content.nextElementSibling
  
  if (contentValue.trim() === 0) {
    
    content.classList.add('is-invalid')
    contentFeedback.textContent = '댓글 내용을 입력해주세요.'
    isValid = false
  } else if (contentValue.length < 10 || contentValue.length > 300) {
    
    content.classList.add('is-invalid')
    contentFeedback.textContent = '댓글은 최소 10자에서 300자 이내로 입력해주세요.'
    isValid = false
  } else {
    
    content.classList.remove('is-invalid')
    contentFeedback.textContent = ''
  }
  
  try {
    
    if (!isValid) throw new Error()
    
    const body = {
      content: contentValue,
    }
    
    const idVal = form.id?.value
    if (idVal) {
      body.id = form.id.value
    }
    
    if (form.articleId?.value) {
      body.articleId = form.articleId.value
    }
    
    if (form.parentCommentId?.value) {
      body.parentCommentId = form.parentCommentId.value
    }
    
    const action = !idVal ? '/comment' : `/comment/${idVal}`
    const method = !idVal ? 'POST' : 'PATCH'
    
    const response = await fetch(action, {
      method,
      headers: window.customJsonHeaders,
      body: JSON.stringify(body),
    })
    
    if (!response.ok) throw new Error()
    
    window.location.reload()
  } catch (err) {
    
    window.showGlobalToast('댓글을 저장하지 못했습니다', 3000)
    
    submitButton.disabled = false
    submitButton.innerHTML = '저장'
    window.isSubmitting = false
  }
}

document.querySelectorAll('.comment-create-form, .comment-update-form, .comment-reply-form').forEach(form => {
  form.addEventListener('submit', onCommentSubmit)
})
document.querySelectorAll('.comment-create-form, .comment-update-form, .comment-reply-form').forEach(form => {
  form.addEventListener('keyDown', window.preventEnterSubmit)
})
