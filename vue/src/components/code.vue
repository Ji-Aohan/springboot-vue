<!-- 这个验证码组件是gpt生成的 -->
<template>
    <div>
      <div class="captcha">
        <canvas ref="captchaCanvas" @click="refreshCaptcha"></canvas>
      </div>
      <button @click="refreshCaptcha">刷新验证码</button>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        captchaText: '',
      };
    },
    mounted() {
      this.refreshCaptcha();
    },
    methods: {
      refreshCaptcha() {
        // 在此处生成新的验证码
        this.captchaText = this.generateRandomCaptcha();
        this.drawCaptcha();
      },
      generateRandomCaptcha() {
        // 生成一个随机的验证码字符串，这只是一个示例，您可以使用更复杂的逻辑
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let captcha = '';
        for (let i = 0; i < 4; i++) {
          captcha += characters.charAt(Math.floor(Math.random() * characters.length));
        }
        return captcha;
      },
      drawCaptcha() {
        const canvas = this.$refs.captchaCanvas;
        const ctx = canvas.getContext('2d');
        const { width, height } = canvas;
  
        // 清除画布
        ctx.clearRect(0, 0, width, height);
  
        // 设置字体和样式
        ctx.font = '20px Arial';
        ctx.fillStyle = 'pink';
  
        // 添加歪曲效果
        for (let i = 0; i < this.captchaText.length; i++) {
          const x = i * (width / this.captchaText.length) + Math.random() * 10;
          const y = height / 2 + Math.random() * 10;
          ctx.fillText(this.captchaText.charAt(i), x, y);
        }
      },
    },
  };
  </script>
  
  <style>
  .captcha {
    text-align: center;
  }
  </style>
  