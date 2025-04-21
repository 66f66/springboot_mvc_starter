$(document).ready(function () {
  
  const contentEl = $('#content')
  
  contentEl.summernote({
    disableDragAndDrop: true,
    minHeight: 300,
    lang: 'ko-KR',
    spellCheck: true,
    placeholder: '내용을 입력하세요. ※이미지는 외부 URL로만 첨부 가능합니다.',
    toolbar: [
      ['style', ['style', 'bold', 'italic', 'underline', 'clear']],
      ['para', ['ul', 'ol', 'paragraph']],
      ['table', ['table']],
      ['insert', ['link', 'picture']],
      ['view', ['fullscreen', 'codeview', 'help']],
    ],
    callbacks: {
      onInit: function () {
        const selectFromFiles = document.querySelector('.note-group-select-from-files')
        if (selectFromFiles) {
          selectFromFiles.remove()
        }
      },
      onPaste: function () {
        setTimeout(() => {
          var code = contentEl.summernote('code')
          if (code.includes('data:image/')) {
            contentEl.summernote('code', code.replace(/<img[^>]+src="data:image\/[^">]+"[^>]*>/g, ''))
            alert('이미지는 URL로만 첨부 가능합니다.')
          }
        }, 10)
      },
    },
  })
})
