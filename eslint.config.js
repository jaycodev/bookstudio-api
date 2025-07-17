import pluginPrettier from 'eslint-plugin-prettier'

export default [
	{
		files: ['src/main/resources/static/**/*.js'],
		ignores: [
			'node_modules/**',
			'target/**',
			'**/*.jsp',
			'**/*.jspf',
			'**/*.java',
		],
		plugins: {
			prettier: pluginPrettier,
		},
		languageOptions: {
			parserOptions: {
				ecmaVersion: 2021,
				sourceType: 'module',
			},
			globals: {
				window: 'readonly',
				document: 'readonly',
				console: 'readonly',
				$: 'readonly',
				jQuery: 'readonly',
				URLSearchParams: 'readonly',
				location: 'readonly',
			},
		},
		rules: {
			'no-var': 'error',
			'prefer-const': 'error',
			'no-console': ['error', { allow: ['warn', 'error'] }],
			'no-unused-vars': ['warn'],
			'no-multiple-empty-lines': ['error', { max: 1 }],
			'no-trailing-spaces': 'error',
			'prettier/prettier': 'error',
		},
	},
]