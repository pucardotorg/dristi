/*
 * Helper jsdocs typedef for formcomposer
 * temporary, can be removed later if desired.
 */


/**
 * @typedef {Object} Populator
 * @property {string} name - The name of the populator.
 * @property {React.ReactNode} [componentInFront] - Optional component to be placed in front of the populator.
 * @property {Object} [validation] - Optional validation rules for the populator.
 * @property {number} [validation.max] - Maximum value.
 * @property {number} [validation.min] - Minimum value.
 * @property {number} [validation.maxlength] - Maximum length.
 * @property {number} [validation.minlength] - Minimum length.
 * @property {Function} [validation.customValidation] - Custom validation function.
 * @property {string} [error] - Error message.
 * @property {Object} [otherProps] - Any other properties.
 */

/**
 * @typedef {Object} Field
 * @property {string} type - The type of the field.
 * @property {string} [label] - The label of the field.
 * @property {boolean} [isMandatory] - If the field is mandatory.
 * @property {boolean} [isInsideBox] - If the field is inside a box.
 * @property {number} [placementinbox] - The placement of the field inside a box.
 * @property {boolean} [withoutLabel] - If the field should be displayed without a label.
 * @property {boolean} [disable] - If the field is disabled.
 * @property {string|React.ReactNode} [component] - The component of the field.
 * @property {string} [description] - The description of the field.
 * @property {React.CSSProperties} [descriptionStyles] - The styles for the description.
 * @property {boolean} [appendColon] - If a colon should be appended to the label.
 * @property {boolean} [hideInForm] - If the field should be hidden in the form.
 * @property {Populator} populators - The populator for the field.
 */

/**
 * @typedef {Object} Section
 * @property {string} [head] - The head of the section.
 * @property {string} [headId] - The ID of the head.
 * @property {string} [subHead] - The subhead of the section.
 * @property {Field[]} body - The body of the section containing fields.
 */

/**
 * @typedef {Object} HorizontalNavConfig
 * @property {string} name - The name of the navigation item.
 * @property {boolean} [activeByDefault] - If the navigation item is active by default.
 */

/**
 * @typedef {Object} FormComposerProps
 * @property {Function} t - The translation function.
 * @property {Object} defaultValues - The default values of the form.
 * @property {Object} [jsonSchema] - The JSON schema for validation.
 * @property {string} [currentFormCategory] - The current form category.
 * @property {Object} [appData] - The application data.
 * @property {Function} [getFormAccessors] - The function to get form accessors.
 * @property {Function} [onFormValueChange] - The function called on form value change.
 * @property {Function} onSubmit - The function called on form submission.
 * @property {Function} onSecondayActionClick - The function called on secondary action click.
 * @property {Object} [customToast] - The custom toast properties.
 * @property {React.CSSProperties} [sectionHeadStyle] - The styles for the section head.
 * @property {boolean} [inline] - If the form should be displayed inline.
 * @property {boolean} [showWrapperContainers] - If the wrapper containers should be shown.
 * @property {boolean} [isDescriptionBold] - If the description should be bold.
 * @property {boolean} [labelBold] - If the label should be bold.
 * @property {boolean} [noBreakLine] - If no break line should be shown.
 * @property {React.CSSProperties} [breaklineStyle] - The styles for the break line.
 * @property {React.CSSProperties} [fieldStyle] - The styles for the fields.
 * @property {React.CSSProperties} [cardStyle] - The styles for the card.
 * @property {boolean} [noBoxShadow] - If no box shadow should be applied.
 * @property {boolean} [isDisabled] - If the form is disabled.
 * @property {HorizontalNavConfig[]} [horizontalNavConfig] - The horizontal navigation configuration.
 * @property {boolean} [childrenAtTheBottom] - If children should be rendered at the bottom.
 * @property {React.ReactNode} [children] - The children elements.
 * @property {React.ReactNode} [heading] - The heading element.
 * @property {string} [cardSubHeaderClassName] - The class name for the card subheader.
 * @property {Function} [updateCustomToast] - The function to update the custom toast.
 * @property {Section[]} config - The configuration of the form sections.
 */

/**
 * @type {FormComposerProps}
 */
export const FormComposerProps = {};

/**
 * @type {Section}
 */
export const Section = {};
