/**
 * @param e {MouseEvent}
 */
async function onVote(e) {
  
  e.preventDefault()
  
  if (window.isSubmitting) return
  window.isSubmitting = true
  
  const articleId = this.getAttribute('data-id')
  
  try {
    
    const response = await fetch(`/article/${articleId}/vote`, {
      method: 'POST',
      headers: window.customJsonHeaders,
    })
    
    if (!response.ok) {
      
      if (response.status === 401) {
        
        window.showGlobalToast('로그인 후에 추천할 수 있습니다', 2000)
        
        window.isSubmitting = false
        
        return
      }
      
      throw new Error()
    }
    
    const data = await response.json()
    const result = data.result
    
    const message = result.voted ? '추천했습니다' : '추천을 취소했습니다'
    window.showGlobalToast(message, 3000)
    
    this.classList.toggle('btn-success', result.voted)
    this.classList.toggle('btn-outline-secondary', !result.voted)
    this.querySelector('.badge').textContent = result.voteCount
    
    window.isSubmitting = false
  } catch (err) {
    
    window.showGlobalToast('현재 추천할 수 없습니다', 3000)
    
    window.isSubmitting = false
  }
}

document.querySelector('#article-vote-btn').addEventListener('click', onVote)
