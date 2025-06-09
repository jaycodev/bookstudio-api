import pluginPrettier from 'eslint-plugin-prettier';

export default [
  {
    files: ['src/main/webapp/**/*.js'],
    ignores: ['**/*.jsp', '**/*.jspf', '**/*.java'],
    languageOptions: {
      ecmaVersion: 2021,
      sourceType: 'module',
      globals: {
        window: 'readonly',
        document: 'readonly',
        console: 'readonly',
      },
    },
    plugins: {
      prettier: pluginPrettier,
    },
    rules: {
      'no-var': 'error',
      'prefer-const': 'warn',
      'prettier/prettier': 'error',
    },
  },
];
