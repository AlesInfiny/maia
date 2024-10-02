import { setLocale } from 'yup';

setLocale({
  mixed: {
    required: '値を入力してください',
  },
  string: {
    email: 'メールアドレスの形式で入力してください',
  },
});
