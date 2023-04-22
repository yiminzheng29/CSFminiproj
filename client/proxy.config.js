const proxy_config = [
    {
      context: [ '/api/**' ],
      target: 'https://whatsnew-production.up.railway.app',
      secure: false
    }
  ]
  
  module.exports = proxy_config