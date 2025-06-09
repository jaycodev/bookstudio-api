import pluginPrettier from 'eslint-plugin-prettier'

export default [
	{
		files: ['src/main/webapp/**/*.js'],

		ignores: [
			'node_modules/**',
			'target/**',
			'**/*.jsp',
			'**/*.jspf',
			'**/*.java',
		],
		languageOptions: {
			parserOptions: {
				ecmaVersion: 2021,
				sourceType: 'module',
			},
			globals: {
				window: 'readonly',
				document: 'readonly',
				console: 'readonly',
			},
		},
		rules: {
			'no-var': 'error',
			'prefer-const': 'error',
			'prettier/prettier': 'error',
			'no-console': ['error', { allow: ['warn', 'error'] }],
			'no-unused-vars': ['warn'],
			'no-undef': 'error',
			'no-multiple-empty-lines': ['error', { max: 1 }],
			'no-trailing-spaces': 'error',
			quotes: ['error', 'single'],
			'space-before-function-paren': ['error', 'never'],
			semi: ['error', 'never'],
		},
	},
]
